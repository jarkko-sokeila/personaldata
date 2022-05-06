package com.sokeila.personaldata;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.sokeila.personaldata.model.Country;
import com.sokeila.personaldata.model.Gender;
import com.sokeila.personaldata.model.Person;
import com.sokeila.personaldata.services.PersonGenerator;
import com.sokeila.personaldata.services.SsnGenerator;
import com.sokeila.personaldata.services.validate.SsnValidator;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.util.Locale;

@Route("")
@JsModule("./js/copytoclipboard.js")
public class MainView extends VerticalLayout {

    private final PersonGenerator personGenerator;

    private final SsnGenerator ssnGenerator;
    private final SsnValidator ssnValidator;

    private TextField ssnField;

    public MainView(PersonGenerator personGenerator, SsnGenerator ssnGenerator, SsnValidator ssnValidator) {
        this.personGenerator = personGenerator;
        this.ssnGenerator = ssnGenerator;
        this.ssnValidator = ssnValidator;

        FormLayout formLayout = new FormLayout();
        formLayout.setMaxWidth("660px");
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 3));
        addSsnGenerator(formLayout);
        addSsnValidator(formLayout);
        add(formLayout);

        add(new Hr());

        addPersonData();
    }

    private void addSsnGenerator(FormLayout formLayout) {
        //HorizontalLayout layout = new HorizontalLayout();

        DatePicker datePicker = new DatePicker("Syntymäpäivä");
        datePicker.setLocale(new Locale("fi"));
        datePicker.setPlaceholder("Anna syntymäpäivä");

        Span ssn = new Span("");
        ssn.getStyle().set("padding-bottom", "10px");
        Button generateSsnButton = new Button("Luo tunnus", event -> {
            LocalDate birthDate = datePicker.getValue();
            if(birthDate != null) {
                String ssnValue = ssnGenerator.generateSsn(Country.FINLAND, Gender.MALE, birthDate);
                ssn.setText(ssnValue);
                if(ssnField != null) {
                    ssnField.setValue(ssnValue);
                }
            } else {
                ssn.setText("Anna syntymäpäivä");
            }
        });

        formLayout.add(datePicker, generateSsnButton, ssn);
        //layout.setVerticalComponentAlignment(Alignment.END, generateSsnButton, ssn);

        //add(layout);
    }

    private void addSsnValidator(FormLayout formLayout) {
        //HorizontalLayout layout = new HorizontalLayout();

        ssnField = new TextField("Henkilötunnus");
        ssnField.setPlaceholder("Anna henkilötunnus");

        Span info = new Span("");
        info.getStyle().set("padding-bottom", "10px");
        Button validateSsnButton = new Button("Tarkista tunnus", event -> {
            String ssn = ssnField.getValue();
            if(StringUtils.isNotBlank(ssn) && ssnValidator.validateSsn(ssn)) {
                info.setText("Henkilötunnus on validi");
            } else {
                info.setText("Henkilötunnus ei ole validi!");
            }
        });

        formLayout.add(ssnField, validateSsnButton, info);
        //layout.setVerticalComponentAlignment(Alignment.END, validateSsnButton, info);

        //add(layout);
    }

    private void addPersonData() {
        VerticalLayout layout = new VerticalLayout();
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setPadding(false);

        TextArea data = new TextArea();
        data.setMinWidth("360px");
        Button generateButton = new Button("Generoi henkilö", event -> {
            Person person = personGenerator.generatePerson();
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            try {
                String json = ow.writeValueAsString(person);
                data.setValue(json);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
        Button copyToClipboard = new Button("Kopioi leikepöydälle", VaadinIcon.COPY.create());
        copyToClipboard.setEnabled(false);
        copyToClipboard.addClickListener(e -> {
            UI.getCurrent().getPage().executeJs("window.copyToClipboard($0)", data.getValue());
            Notification.show("Kopioitu leikepöydälle", 5000, Notification.Position.BOTTOM_START);
        });

        data.addValueChangeListener(event -> {
           String value = event.getValue();
            copyToClipboard.setEnabled(StringUtils.isNotBlank(value));
        });

        horizontalLayout.add(generateButton, copyToClipboard);
        layout.addAndExpand(horizontalLayout, data);
        layout.setPadding(false);

        add(layout);
    }
}
