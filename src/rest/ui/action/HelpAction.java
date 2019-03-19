package rest.ui.action;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.wm.ToolWindow;

/**
 * RESTMan
 * rest.ui.action
 * 帮助
 *
 * @author BlueDriver
 * @email cpwu@foxmail.com
 * @date 2019/03/12 16:25 Tuesday
 */
public class HelpAction extends AnAction {
    public static ToolWindow toolWindow;
    public static Project project;
//    private static ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();

    public HelpAction(ToolWindow toolWindow, Project project) {
        //todo: 2019-03-19
        super("Help", "Help", AllIcons.Actions.Help);
        HelpAction.toolWindow = toolWindow;
        HelpAction.project = project;
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        String message = "Hello";
        ApplicationManager.getApplication().invokeLater(() -> Messages.showErrorDialog(message, "RESTMan"));

    }
}
