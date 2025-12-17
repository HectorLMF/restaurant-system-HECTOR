package es.ull.esit.app;

import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the AboutUs Swing window.
 */
public class AboutUsTest {

  @Test
  void testInitComponents_textAndButton() throws Exception {
    AboutUs about = new AboutUs();
    try {
      Field txtField = AboutUs.class.getDeclaredField("ourStory");
      txtField.setAccessible(true);
      JTextArea ourStory = (JTextArea) txtField.get(about);

      Field btnField = AboutUs.class.getDeclaredField("jButton3");
      btnField.setAccessible(true);
      JButton btn = (JButton) btnField.get(about);

      // Basic checks
      assertEquals("Info", about.getTitle());
      assertEquals("Go Back", btn.getText());
      assertNotNull(ourStory);
      assertFalse(ourStory.isEditable());

      String txt = ourStory.getText();
      assertNotNull(txt);

      // JTextArea does not render HTML; content is plain text.
      String normalized = txt.trim();
      assertFalse(normalized.isEmpty());
      assertTrue(normalized.startsWith("Our Story"));
      assertTrue(normalized.contains("Our Story"));
    } finally {
      about.dispose();
    }
  }

  @Test
  void testGoBack_disposesWindow() throws Exception {
    AboutUs about = new AboutUs();
    try {
      Method m = AboutUs.class.getDeclaredMethod("jButton3ActionPerformed", java.awt.event.ActionEvent.class);
      m.setAccessible(true);

      m.invoke(about, (Object) null);

      // After action, the AboutUs frame should be disposed
      assertFalse(about.isDisplayable());
    } finally {
      // Safe even if already disposed
      about.dispose();
    }
  }
}
