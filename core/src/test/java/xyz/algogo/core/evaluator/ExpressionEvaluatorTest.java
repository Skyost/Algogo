package xyz.algogo.core.evaluator;

import ch.obermuhlner.math.big.BigDecimalMath;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import xyz.algogo.core.evaluator.atom.Atom;
import xyz.algogo.core.evaluator.atom.NumberAtom;
import xyz.algogo.core.evaluator.context.EvaluationContext;
import xyz.algogo.core.evaluator.expression.Expression;
import xyz.algogo.core.evaluator.function.Function;
import xyz.algogo.core.evaluator.variable.Variable;
import xyz.algogo.core.evaluator.variable.VariableType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

class ExpressionEvaluatorTest {

	private static final Logger logger = Logger.getLogger(ExpressionEvaluatorTest.class.getName());

	private static ExpressionEvaluator evaluator = new ExpressionEvaluator();
	private static EvaluationContext context;

	@BeforeAll
	static void initAll() {
		context = new EvaluationContext((source, arguments) -> "", (source, content) -> logger.info(content));
	}

	@AfterEach
	void clearVariables() {
		evaluator.clearVariables();
		evaluator.clearFunctions();
		evaluator.addDefaultVariables();
		evaluator.addDefaultFunctions();
	}

	@Test
	void variableTest() {
		final Variable variable = new Variable("a");
		evaluator.putVariable(new Variable("a"));
		assertTrue(evaluator.hasVariable(variable.getIdentifier()));
	}

	@Test
	void variableValueTest() {
		final Variable variable = new Variable("a");
		evaluator.putVariable(variable);
		variable.setValue(evaluator.evaluate("2 + 2 * 4").getValue());
		assertEquals(new BigDecimal(10), evaluator.evaluate("a").getValue());
	}

	@Test
	void contextTest() {
		assertEquals(evaluator.evaluate(Expression.parse("pi"), context).getValue(), BigDecimalMath.pi(context.getMathContext()));
	}

	@Test
	void additiveExpressionTest() {
		assertEquals(new BigDecimal(18), evaluator.evaluate("8 + 10").getValue());
		assertEquals(new BigDecimal(-2), evaluator.evaluate("8 - 10").getValue());

		assertNotEquals(new BigDecimal(21), evaluator.evaluate("7 * 2 + 1").getValue());
	}

	@Test
	void multiplicationExpressionTest() {
		assertEquals(new BigDecimal(600), evaluator.evaluate("6 * 100").getValue());
		assertEquals(new BigDecimal("0.2"), evaluator.evaluate("1 / 5").getValue());
		assertEquals(BigDecimal.ONE, evaluator.evaluate("10 % 3").getValue());
	}

	@Test
	void parenthesisExpressionTest() {
		assertEquals(new BigDecimal(20), evaluator.evaluate("5 * (2 + 2)").getValue());
		assertNotEquals(new BigDecimal(20), evaluator.evaluate("5 * 2 + 2").getValue());

		assertEquals(new BigDecimal(10), evaluator.evaluate("|-15 + 5|").getValue());
		assertEquals(new BigDecimal(25), evaluator.evaluate("|5^2|").getValue());
	}

	@Test
	void powerExpressionTest() {
		assertEquals(evaluator.evaluate("EXP(1)").getValue(), evaluator.evaluate("e^1").getValue());
		assertEquals(new BigDecimal(17), evaluator.evaluate("1 + 2^4").getValue());
	}

	@Test
	void functionExpressionTest() {
		evaluator.putFunction(new Function("FUNCTION") {

			@Override
			public final Atom evaluate(final EvaluationContext context, final Atom... arguments) {
				final BigDecimal x = (BigDecimal)arguments[0].getValue();
				return new NumberAtom(x.pow(2).subtract(x).subtract(BigDecimal.ONE));
			}

		});

		assertEquals(BigDecimal.ZERO.setScale(10, RoundingMode.HALF_DOWN), ((BigDecimal)evaluator.evaluate("FUNCTION((1 + SQRT(5)) / 2)").getValue()).setScale(10, RoundingMode.HALF_DOWN));
		assertEquals(evaluator.evaluate("SQRT(2)").getValue(), evaluator.evaluate("ROOT(2, 2)").getValue());
		assertEquals(new BigDecimal(3628800), evaluator.evaluate("FACTORIAL(10)").getValue());
		assertEquals(new BigDecimal("0.6931471805599453"), evaluator.evaluate("LOG(2)").getValue());
		assertEquals(new BigDecimal("23.14069263277926"), evaluator.evaluate("EXP(pi)").getValue());
		assertEquals(new BigDecimal(100), evaluator.evaluate("MAX(1 + 1, SQRT(2), 50 * 2)").getValue());
		assertEquals(evaluator.evaluate("| 10 - 20 |").getValue(), evaluator.evaluate("ABS(10 - 20)").getValue());
	}

	@Test
	void stringExpressionTest() {
		evaluator.putVariable(new Variable("a", VariableType.STRING, "Hello"));
		evaluator.putVariable(new Variable("b", VariableType.STRING, "World"));
		assertEquals("Hello World !", evaluator.evaluate("a + \" \" + b + \" !\"").getValue());
	}

	@Test
	void booleanExpressionTest() {
		assertEquals(BigDecimal.ONE, evaluator.evaluate("2 + 2 == 2^2").getValue());
		assertEquals(BigDecimal.ONE, evaluator.evaluate("(2 + 2 != 4 OR 2 + 3 == 5) && (4^2 == 16 OR 4 % 2 == 1)").getValue());
		assertEquals(BigDecimal.ONE, evaluator.evaluate("|-2 + 2| == 1 || |-2 + 2| == 0").getValue());
		assertEquals(BigDecimal.ONE, evaluator.evaluate("NOT (true AND false)").getValue());
		assertEquals(BigDecimal.ONE, evaluator.evaluate("!false").getValue());
		assertEquals(BigDecimal.ZERO, evaluator.evaluate("!true").getValue());
	}

}
