package xyz.algogo.core;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import xyz.algogo.core.exception.ParseException;
import xyz.algogo.core.language.AlgogoLanguage;

import java.io.File;
import java.nio.file.Files;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

class AlgorithmTest {

	private static final Logger logger = Logger.getLogger(AlgorithmTest.class.getName());

	private static String validFile;
	private static String invalidFile;

	@BeforeAll
	static void initAll() {
		try {
			final File resourcesDirectory = new File("src/test/resources/");

			validFile = new String(Files.readAllBytes(new File(resourcesDirectory, "ValidTest.agg2").toPath()));
			invalidFile = new String(Files.readAllBytes(new File(resourcesDirectory, "InvalidTest.agg2").toPath()));
		}
		catch(final Exception ex) {
			ex.printStackTrace();
		}
	}

	@Test
	void validParseTest() {
		final Algorithm valid = Algorithm.parse(validFile);
		assertEquals(validFile, valid.toLanguage(new AlgogoLanguage()));
	}

	@Test
	void invalidParseTest() {
		assertThrows(ParseException.class, () -> Algorithm.parse(invalidFile));
	}

	@Test
	void creditsTest() {
		final Algorithm valid = Algorithm.parse(validFile);
		assertEquals("ValidTest", valid.getTitle());
		assertEquals("Skyost", valid.getAuthor());
	}

	@Test
	void evalTest() {
		final Algorithm valid = Algorithm.parse(validFile);
		assertNull(valid.evaluate((source, arguments) -> "4^2 * 2", (source, content) -> logger.info(content)));
	}

}
