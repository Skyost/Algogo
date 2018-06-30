package xyz.algogo.core.evaluator;

import ch.obermuhlner.math.big.BigDecimalMath;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import xyz.algogo.core.evaluator.context.EvaluationContext;
import xyz.algogo.core.evaluator.expression.Expression;
import xyz.algogo.core.evaluator.variable.Variable;
import xyz.algogo.core.evaluator.variable.VariableType;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

class ExpressionEvaluatorTest {

	private static final Logger logger = Logger.getLogger(ExpressionEvaluatorTest.class.getName());

	private static ExpressionEvaluator evaluator;

	@BeforeAll
	static void initAll() {
		evaluator = new ExpressionEvaluator((source, arguments) -> "", (source, content) -> logger.info(content));
	}

	@AfterEach
	void clearVariables() {
		evaluator.clearVariables();
		evaluator.addDefaultVariables();
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
		final EvaluationContext context = new EvaluationContext(MathContext.DECIMAL32);
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
	void powerExpressionTest() {
		assertEquals(evaluator.evaluate("EXP(1)").getValue(), evaluator.evaluate("e^1").getValue());
		assertEquals(new BigDecimal(17), evaluator.evaluate("1 + 2^4").getValue());
	}

	@Test
	void functionExpressionTest() {
		assertEquals(evaluator.evaluate("SQRT(2)").getValue(), evaluator.evaluate("ROOT(2, 2)").getValue());
		assertEquals(new BigDecimal(3628800), evaluator.evaluate("FACTORIAL(10)").getValue());
		assertEquals(new BigDecimal("0.6931471805599453"), evaluator.evaluate("LOG(2)").getValue());
		assertEquals(new BigDecimal("23.14069263277926"), evaluator.evaluate("EXP(pi)").getValue());
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
		assertEquals(BigDecimal.ONE, evaluator.evaluate("(2 + 2 != 4 || 2 + 3 == 5) && (4^2 == 16 || 4 % 2 == 1)").getValue());
		assertEquals(BigDecimal.ONE, evaluator.evaluate("!false").getValue());
		assertEquals(BigDecimal.ZERO, evaluator.evaluate("!true").getValue());
	}

}