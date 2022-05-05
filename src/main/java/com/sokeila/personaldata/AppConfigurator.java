package com.sokeila.personaldata;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.server.PWA;

@Viewport("width=device-width, minimum-scale=1.0, initial-scale=1.0, user-scalable=yes")
@PWA(name = "Personal data generator", shortName = "Personal data generator")
public class AppConfigurator implements AppShellConfigurator {
}
