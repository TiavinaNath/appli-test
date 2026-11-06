package com.example.applitest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

class LecturesPageTest {
  private static final String VALEUR = "valeur-jetable";

  @Test
  void lecture_never_keeps_the_value() {
    var lecture = Lecture.of("forme", VALEUR);

    assertEquals(VALEUR.length(), lecture.longueur());
    assertEquals(8, lecture.empreinte().length());
    assertFalse(lecture.toString().contains(VALEUR));
  }

  @Test
  void absent_value_has_neither_length_nor_digest() {
    var lecture = Lecture.of("forme", null);

    assertFalse(lecture.presente());
    assertNull(lecture.empreinte());
  }

  @Test
  void four_identical_reads_are_reported_as_identical() {
    var lectures =
        List.of(
            Lecture.of("a", VALEUR),
            Lecture.of("b", VALEUR),
            Lecture.of("c", VALEUR),
            Lecture.of("d", VALEUR));

    var page = LecturesPage.render(lectures);

    assertTrue(page.contains("Les quatre lectures voient la même valeur."));
    assertFalse(page.contains(VALEUR));
  }

  @Test
  void one_missing_read_is_reported_as_different() {
    var lectures =
        List.of(
            Lecture.of("a", VALEUR),
            Lecture.of("b", VALEUR),
            Lecture.of("c", null),
            Lecture.of("d", VALEUR));

    assertEquals("✗ Les lectures diffèrent.", LecturesPage.verdict(lectures));
  }

  @Test
  void no_read_at_all_is_reported_as_absent() {
    var lectures = List.of(Lecture.of("a", null), Lecture.of("b", null));

    assertEquals("Variable absente.", LecturesPage.verdict(lectures));
  }
}
