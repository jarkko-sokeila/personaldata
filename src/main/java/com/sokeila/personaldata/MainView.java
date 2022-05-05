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
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
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

    private PersonGenerator personGenerator;

    private final SsnGenerator ssnGenerator;
    private final SsnValidator ssnValidator;

    public MainView(PersonGenerator personGenerator, SsnGenerator ssnGenerator, SsnValidator ssnValidator) {
        this.personGenerator = personGenerator;
        this.ssnGenerator = ssnGenerator;
        this.ssnValidator = ssnValidator;

        addSsnGenerator();
        addSsnValidator();
        add(new Hr());
        addPersonData();
    }

    private void addSsnGenerator() {
        HorizontalLayout layout = new HorizontalLayout();

        DatePicker datePicker = new DatePicker("Birth date");
        datePicker.setLocale(new Locale("fi"));

        Span ssn = new Span("");
        ssn.getStyle().set("padding-bottom", "10px");
        Button generateSsnButton = new Button("Generate ssn", event -> {
            LocalDate birthDate = datePicker.getValue();
            if(birthDate != null) {
                String ssnValue = ssnGenerator.generateSsn(Country.FINLAND, Gender.MALE, birthDate);
                ssn.setText(ssnValue);
            } else {
                ssn.setText("Set birthdate first");
            }
        });

        layout.add(datePicker, generateSsnButton, ssn);
        layout.setVerticalComponentAlignment(Alignment.END, generateSsnButton, ssn);

        add(layout);
    }

    private void addSsnValidator() {
        HorizontalLayout layout = new HorizontalLayout();

        TextField ssnField = new TextField("Ssn");
        ssnField.setPlaceholder("Give ssn");

        Span info = new Span("");
        info.getStyle().set("padding-bottom", "10px");
        Button validateSsnButton = new Button("Validate ssn", event -> {
            String ssn = ssnField.getValue();
            if(StringUtils.isNotBlank(ssn) && ssnValidator.validateSsn(ssn)) {
                info.setText("Ssn is valid");
            } else {
                info.setText("Not valid");
            }
        });

        layout.add(ssnField, validateSsnButton, info);
        layout.setVerticalComponentAlignment(Alignment.END, validateSsnButton, info);

        add(layout);
    }

    private void addPersonData() {
        VerticalLayout layout = new VerticalLayout();

        TextArea data = new TextArea();
        data.setMinWidth("360px");
        Button generateButton = new Button("Generate person data", event -> {
            Person person = personGenerator.generatePerson();
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            try {
                String json = ow.writeValueAsString(person);
                data.setValue(json);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
        Button copyToClipboard = new Button("Copy to clipboard", VaadinIcon.COPY.create());
        copyToClipboard.setEnabled(false);
        copyToClipboard.addClickListener(
                e -> UI.getCurrent().getPage().executeJs("window.copyToClipboard($0)", data.getValue())
        );

        data.addValueChangeListener(event -> {
           String value = event.getValue();
            copyToClipboard.setEnabled(StringUtils.isNotBlank(value));
        });

        layout.addAndExpand(generateButton, copyToClipboard, data);
        layout.setPadding(false);

        add(layout);
    }
}
