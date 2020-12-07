/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.service.istat;

import com.md.nasutils.model.Telemetry;
import com.md.nasutils.service.http.NasConfiguration;

/** 
 * @author michaeldoyle
 */
public interface ReadyNasIstatService {

    Telemetry getTelemetry(NasConfiguration config);
    
    Telemetry getTelemetrySince(NasConfiguration config, long since);

}
