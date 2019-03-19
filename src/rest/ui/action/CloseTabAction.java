package rest.ui.action;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;

/**
 * RESTMan
 * rest.ui.action
 * 关闭当前的Tab
 *
 * @author BlueDriver
 * @email cpwu@foxmail.com
 * @date 2019/03/12 16:25 Tuesday
 */
public class CloseTabAction extends AnAction {
    private ToolWindow toolWindow;
    private Project project;

//    private static ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();

    public CloseTabAction(ToolWindow toolWindow, Project project) {
        super("Close Tab", "Close selected Tab", AllIcons.Actions.Delete);
        this.toolWindow = toolWindow;
        this.project = project;
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        toolWindow.getContentManager().removeContent(
                toolWindow.getContentManager().getSelectedContent(), true
        );
    }
}
