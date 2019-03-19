package rest.ui.action;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.content.Content;
import rest.component.RestComponent;

/**
 * RESTMan
 * rest.ui.action
 * 增加Tab
 *
 * @author BlueDriver
 * @email cpwu@foxmail.com
 * @date 2019/03/12 16:25 Tuesday
 */
public class AddTabAction extends AnAction {
    private ToolWindow toolWindow;
    private Project project;
    //    private static ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();


    public AddTabAction(ToolWindow toolWindow, Project project) {
        super("Add Tab", "Create a new Tab", AllIcons.General.Add);
        this.toolWindow = toolWindow;
        this.project = project;
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        Content[] contents = toolWindow.getContentManager().getContents();
        int index = contents.length + 1;
        String title = "Tab" + index;
        for (Content content : contents) {
            if (title.equals(content.getToolwindowTitle())) {
                title = title + "-" + index;
                break;
            }
        }
        RestComponent component = RestComponent.getInstance(project);
        component.initRestComponent(toolWindow, title);
    }
}
