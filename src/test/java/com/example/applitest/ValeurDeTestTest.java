package com.example.applitest;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.util.stream.Collectors.joining;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

/**
 * Runs only where POJA hands the test value to the tests: on the CI of preprod and prod, once the
 * variable has a test value. The import and the first deployment run without it.
 */
@EnabledIfEnvironmentVariable(named = LecturesController.NOM, matches = ".+")
@SpringJUnitConfig(LecturesController.class)
class ValeurDeTestTest {
  @Autowired private LecturesController controller;

  @Test
  void the_four_reads_see_the_test_value() throws IOException {
    var lectures = controller.lectures();

    publishToGithubRunSummary(lectures);
    assertTrue(LecturesPage.identiques(lectures), LecturesPage.verdict(lectures));
  }

  private static void publishToGithubRunSummary(List<Lecture> lectures) throws IOException {
    var summaryFile = System.getenv("GITHUB_STEP_SUMMARY");
    if (summaryFile == null) {
      return;
    }
    var rows =
        lectures.stream()
            .map(
                l ->
                    "| `%s` | %s | `%s` |"
                        .formatted(
                            l.forme(),
                            l.presente() ? l.longueur() : "absente",
                            l.presente() ? l.empreinte() : "–"))
            .collect(joining("\n"));
    var summary =
        """
        ### Valeur de test de MY_API_KEY, lue par les tests

        | Lecture | Longueur | Empreinte |
        |---|---|---|
        %s

        **%s**
        """
            .formatted(rows, LecturesPage.verdict(lectures));
    Files.writeString(Path.of(summaryFile), summary, UTF_8, CREATE, APPEND);
  }
}
