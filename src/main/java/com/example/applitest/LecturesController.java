package com.example.applitest;

import static org.springframework.http.MediaType.TEXT_HTML_VALUE;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LecturesController {
  static final String NOM = "MY_API_KEY";
  private static final String ABSENTE = "<absente>";

  private final Environment environment;
  private final String valueEnMajuscules;
  private final String valueEnNotationPointee;

  public LecturesController(
      Environment environment,
      @Value("${MY_API_KEY:" + ABSENTE + "}") String valueEnMajuscules,
      @Value("${my.api.key:" + ABSENTE + "}") String valueEnNotationPointee) {
    this.environment = environment;
    this.valueEnMajuscules = valueEnMajuscules;
    this.valueEnNotationPointee = valueEnNotationPointee;
  }

  public List<Lecture> lectures() {
    return List.of(
        Lecture.of("System.getenv(\"MY_API_KEY\")", System.getenv(NOM)),
        Lecture.of("@Value(\"${MY_API_KEY}\")", nullIfAbsente(valueEnMajuscules)),
        Lecture.of("@Value(\"${my.api.key}\")", nullIfAbsente(valueEnNotationPointee)),
        Lecture.of("Environment.getProperty(\"MY_API_KEY\")", environment.getProperty(NOM)));
  }

  @GetMapping(value = "/lectures", produces = TEXT_HTML_VALUE)
  public String page() {
    return LecturesPage.render(lectures());
  }

  private static String nullIfAbsente(String value) {
    return ABSENTE.equals(value) ? null : value;
  }
}
