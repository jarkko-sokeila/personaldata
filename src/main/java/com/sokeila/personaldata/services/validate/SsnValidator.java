package com.sokeila.personaldata.services.validate;

import com.sokeila.personaldata.services.SsnGenerator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SsnValidator {
    private static final Logger logger = LoggerFactory.getLogger(SsnValidator.class);

    public boolean validateSsn(String ssn) {
        try {
            if (StringUtils.isNotBlank(ssn) && ssn.trim().length() == 11) {
                ssn = ssn.trim();
                char checkSign = ssn.charAt(10);
                char decade = ssn.charAt(6);
                String numberPart = ssn.substring(0, 6) + ssn.substring(7, 10);

                int remainder = Integer.parseInt(numberPart) % 31;

                return checkSign == SsnGenerator.checkSigns[remainder];
            }
        } catch (Exception e) {
            logger.error("Could not validate ssn: {}", ssn);
        }

        return false;
    }
}
