package rest.ui.action;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import rest.ui.window.RESTWindow;

/**
 * RESTMan
 * rest.ui.action
 * 发送请求
 *
 * @author BlueDriver
 * @email cpwu@foxmail.com
 * @date 2019/03/12 16:25 Tuesday
 */
public class ExecuteAction extends AnAction {
    private ToolWindow toolWindow;
    private Project project;
    private RESTWindow RESTWindow;
//    private static ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();

    public ExecuteAction(ToolWindow toolWindow, Project project, RESTWindow RESTWindow) {
        super("Send", "Send", AllIcons.Actions.Execute);
        this.toolWindow = toolWindow;
        this.project = project;
        this.RESTWindow = RESTWindow;
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        RESTWindow.send();
    }
}
