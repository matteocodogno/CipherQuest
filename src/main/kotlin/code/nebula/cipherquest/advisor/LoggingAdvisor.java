package code.nebula.cipherquest.advisor;

import org.slf4j.*;
import org.springframework.ai.chat.client.*;
import org.springframework.ai.chat.model.*;

import java.util.*;

public class LoggingAdvisor implements RequestResponseAdvisor {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAdvisor.class);

    @Override
    public AdvisedRequest adviseRequest(AdvisedRequest request, Map<String, Object> context) {
        logger.info("Request: " + request);
        return request;
    }

    @Override
    public ChatResponse adviseResponse(ChatResponse response, Map<String, Object> context) {
		logger.info("Token: " + response.getMetadata().getUsage().getTotalTokens());

        return response;
    }
}
