package rest.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import org.jetbrains.annotations.NotNull;
import rest.component.RestComponent;

/**
 * RESTMan
 * rest.ui
 *
 * @author BlueDriver
 * @email cpwu@foxmail.com
 * @date 2019/03/12 16:53 Tuesday
 */
public class RestManToolWindowFactory implements ToolWindowFactory {
    public static final String TOOL_ID = "RESTMan";
    public static ToolWindow toolWindow;
    public static Project project;

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        RestManToolWindowFactory.toolWindow = toolWindow;
        RestManToolWindowFactory.project = project;
        toolWindow.setToHideOnEmptyContent(true);
        RestComponent component = RestComponent.getInstance(project);
        component.initRestComponent(toolWindow, "Tab1");
    }


}
