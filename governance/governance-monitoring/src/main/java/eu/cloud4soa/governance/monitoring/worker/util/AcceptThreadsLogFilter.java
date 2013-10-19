/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.cloud4soa.governance.monitoring.worker.util;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;


/**
 *
 * @author frarav
 */
public class AcceptThreadsLogFilter extends Filter<ILoggingEvent> {
    private Level level;
    private String logger;
    
        
    @Override
    public FilterReply decide(ILoggingEvent event) {  
        
        if (event.getThreadName().contains("ThreadPoolTaskScheduler")) {
            return FilterReply.NEUTRAL;
        } else {
            return FilterReply.DENY;
        }
    }
    

    
    public void setLevel(Level level) {
        this.level = level;
    }

    
    
    public void setLogger(String logger) {
        this.logger = logger;
    }

    
    
    public void start() {
        if (this.level != null && this.logger != null) {
            super.start();
        }
    }
    
    
    
}
