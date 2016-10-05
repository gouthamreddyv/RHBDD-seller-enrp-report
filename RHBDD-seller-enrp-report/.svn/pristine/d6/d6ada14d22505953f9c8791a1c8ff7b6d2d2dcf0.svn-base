package net.tao.rhb.dd.enrpsellerreport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class LogItemWriter<T> implements ItemWriter<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogItemWriter.class);

    @Override
    public void write(List<? extends T> items) throws Exception {
        for (T item : items) {
            LOGGER.info("{}", item);
        }
    }
}
