package ru.compscicenter.edide.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import ru.compscicenter.edide.StudyEditor;
import ru.compscicenter.edide.StudyTaskManager;
import ru.compscicenter.edide.course.TaskFile;
import ru.compscicenter.edide.course.Window;

/**
 * author: liana
 * data: 6/30/14.
 */
public class PrevWindowAction extends AnAction {
  public void actionPerformed(AnActionEvent e) {
    Project project = e.getProject();
    Editor selectedEditor = StudyEditor.getSelectedEditor(project);
    if (selectedEditor != null) {
      FileDocumentManager fileDocumentManager = FileDocumentManager.getInstance();
      VirtualFile openedFile = fileDocumentManager.getFile(selectedEditor.getDocument());
      if (openedFile != null) {
        StudyTaskManager taskManager = StudyTaskManager.getInstance(project);
        TaskFile selectedTaskFile = taskManager.getTaskFile(openedFile);
        if (selectedTaskFile != null) {
          selectedTaskFile.updateOffsets(selectedEditor);
          Window selectedWindow = selectedTaskFile.getSelectedWindow();
          Window prev = null;
          for (Window window : selectedTaskFile.getWindows()) {
            if (window == selectedWindow) {
              break;
            }
            prev = window;
          }

          if (prev != null) {
            selectedEditor.getMarkupModel().removeAllHighlighters();
            selectedTaskFile.setSelectedWindow(prev);
            prev.draw(selectedEditor, !prev.isResolveStatus(), true);
            return;
          }
          //TODO:propose user to do next action
        }
      }
    }
  }
}
