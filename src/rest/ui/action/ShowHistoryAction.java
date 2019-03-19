package rest.ui.action;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;

/**
 * RESTMan
 * rest.ui.action
 * 显示历史
 *
 * @author BlueDriver
 * @email cpwu@foxmail.com
 * @date 2019/03/12 16:25 Tuesday
 */
public class ShowHistoryAction extends AnAction {
    public static ToolWindow toolWindow;
    public static Project project;

    public ShowHistoryAction(ToolWindow toolWindow, Project project) {
        //todo: 2019-03-18
        super("History", "Show History", AllIcons.Actions.Annotate);
        ShowHistoryAction.toolWindow = toolWindow;
        ShowHistoryAction.project = project;
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {

    }
}
