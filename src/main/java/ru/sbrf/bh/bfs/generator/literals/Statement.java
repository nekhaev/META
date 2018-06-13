package ru.sbrf.bh.bfs.generator.literals;

/**
 * Created by sbt-barsukov-sv on 28.05.2018.
 */
public interface Statement {
    String LOGGER_INFO_LEVEL = "LOGGER.info($S)";
    String LOGGER_DEBUG_LEVEL = "LOGGER.debug($S)";
    String LOGGER_ERROR_LEVEL = "LOGGER.error($S,e)";

    String MONITORING_SERVICE_START = "long startTime = monitoringService.start($S)";
    String MONITORING_SERVICE_STOP = "monitoringService.stop($S,$S,startTime)";

    String EXCEPTION_THROW = "throw e";

    String INITIALIZE_SUCCESS_FLAG = "boolean success = false";
    String CHANGE_SUCCESS_FLAG = "success = true";

    String INITIALIZE_RESPONSE = "$T rs = null";
    String RETURN_RESPONSE = "return rs";
    String DA_SEND_REQUEST = "rs = $L.sendRequest(new $L<>(rq)).getMessage()";
    String FG_UPDATE_RESPONSE = "rs = $L.$L(rq)";
    String INITIALIZE_REQUEST = "$T rq = null";
}
