/*
 *  Copyright 2013 Cloud4SOA, www.cloud4soa.eu
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.cloud4soa.repository.utils;

import eu.cloud4soa.api.datamodel.semantic.measure.ComputingUnit;
import eu.cloud4soa.api.datamodel.semantic.measure.GigaByte;
import eu.cloud4soa.api.datamodel.semantic.measure.GigaByteperSecond;
import eu.cloud4soa.api.datamodel.semantic.measure.GigaHertz;
import eu.cloud4soa.api.datamodel.semantic.measure.Hour;
import eu.cloud4soa.api.datamodel.semantic.measure.KiloByte;
import eu.cloud4soa.api.datamodel.semantic.measure.KiloByteperSecond;
import eu.cloud4soa.api.datamodel.semantic.measure.KiloHertz;
import eu.cloud4soa.api.datamodel.semantic.measure.MegaByte;
import eu.cloud4soa.api.datamodel.semantic.measure.MegaByteperSecond;
import eu.cloud4soa.api.datamodel.semantic.measure.MegaHertz;
import eu.cloud4soa.api.datamodel.semantic.measure.MilliSecond;
import eu.cloud4soa.api.datamodel.semantic.measure.Minute;
import eu.cloud4soa.api.datamodel.semantic.measure.NetworkingUnit;
import eu.cloud4soa.api.datamodel.semantic.measure.Second;
import eu.cloud4soa.api.datamodel.semantic.measure.StorageUnit;
import eu.cloud4soa.api.datamodel.semantic.measure.TeraByte;
import eu.cloud4soa.api.datamodel.semantic.measure.TeraByteperSecond;
import eu.cloud4soa.api.datamodel.semantic.measure.TimeUnit;

/**
 *
 * @author zeginis
 */
public class MeasurementUnitConverter {

    public static float convertTime2Hours(TimeUnit time) {
        float hours = 0;
        if (time instanceof Hour) {
            hours = time.getValue();
        } else if (time instanceof Minute) {
            hours = time.getValue() / 60;

        } else if (time instanceof Second) {
            hours = time.getValue() / 3600;

        } else if (time instanceof MilliSecond) {
            hours = time.getValue() / 3600000;
        }
        return hours;
    }

    public static float convertTime2Minutes(TimeUnit time) {
        float minutes = 0;
        if (time instanceof Hour) {
            minutes = time.getValue() * 60;
        } else if (time instanceof Minute) {
            minutes = time.getValue();

        } else if (time instanceof Second) {
            minutes = time.getValue() / 60;

        } else if (time instanceof MilliSecond) {
            minutes = time.getValue() / 60000;
        }
        return minutes;
    }

    public static float convertTime2Second(TimeUnit time) {
        float seconds = 0;
        if (time instanceof Hour) {
            seconds = time.getValue() * 3600;
        } else if (time instanceof Minute) {
            seconds = time.getValue() * 60;

        } else if (time instanceof Second) {
            seconds = time.getValue();

        } else if (time instanceof MilliSecond) {
            seconds = time.getValue() / 1000;
        }
        return seconds;
    }

    public static float convertTime2MilliSecond(TimeUnit time) {
        float milliseconds = 0;
        if (time instanceof Hour) {
            milliseconds = time.getValue() * 3600000;
        } else if (time instanceof Minute) {
            milliseconds = time.getValue() * 60000;

        } else if (time instanceof Second) {
            milliseconds = time.getValue() * 1000;

        } else if (time instanceof MilliSecond) {
            milliseconds = time.getValue();
        }
        return milliseconds;
    }

    public static float convertBandwidth2TerabytePerSecond(NetworkingUnit bandwidth) {
        float terabytepersecond = 0;
        if (bandwidth instanceof TeraByteperSecond) {
            terabytepersecond = bandwidth.getValue();
        } else if (bandwidth instanceof GigaByteperSecond) {
            terabytepersecond = bandwidth.getValue() / 1000;

        } else if (bandwidth instanceof MegaByteperSecond) {
            terabytepersecond = bandwidth.getValue() / 1000000;

        } else if (bandwidth instanceof KiloByteperSecond) {
            terabytepersecond = bandwidth.getValue() / 1000000000;
        }
        return terabytepersecond;
    }

    public static float convertBandwidth2GigabytePerSecond(NetworkingUnit bandwidth) {
        float gigabytepersecond = 0;
        if (bandwidth instanceof TeraByteperSecond) {
            gigabytepersecond = bandwidth.getValue() * 1000;
        } else if (bandwidth instanceof GigaByteperSecond) {
            gigabytepersecond = bandwidth.getValue();

        } else if (bandwidth instanceof MegaByteperSecond) {
            gigabytepersecond = bandwidth.getValue() / 1000;

        } else if (bandwidth instanceof KiloByteperSecond) {
            gigabytepersecond = bandwidth.getValue() / 1000000;
        }
        return gigabytepersecond;
    }

    public static float convertBandwidth2MegabytePerSecond(NetworkingUnit bandwidth) {
        float megabytepersecond = 0;
        if (bandwidth instanceof TeraByteperSecond) {
            megabytepersecond = bandwidth.getValue() * 1000000;
        } else if (bandwidth instanceof GigaByteperSecond) {
            megabytepersecond = bandwidth.getValue() * 1000;

        } else if (bandwidth instanceof MegaByteperSecond) {
            megabytepersecond = bandwidth.getValue();

        } else if (bandwidth instanceof KiloByteperSecond) {
            megabytepersecond = bandwidth.getValue() / 1000;
        }
        return megabytepersecond;
    }

    public static float convertBandwidth2KilobytePerSecond(NetworkingUnit bandwidth) {
        float kilobytepersecond = 0;
        if (bandwidth instanceof TeraByteperSecond) {
            kilobytepersecond = bandwidth.getValue() * 1000000000;
        } else if (bandwidth instanceof GigaByteperSecond) {
            kilobytepersecond = bandwidth.getValue() * 1000000;

        } else if (bandwidth instanceof MegaByteperSecond) {
            kilobytepersecond = bandwidth.getValue() * 1000;

        } else if (bandwidth instanceof KiloByteperSecond) {
            kilobytepersecond = bandwidth.getValue();
        }
        return kilobytepersecond;
    }

    public static float convertStorage2Terabyte(StorageUnit storage) {
        float terabyte = 0;
        if (storage instanceof TeraByte) {
            terabyte = storage.getValue();
        } else if (storage instanceof GigaByte) {
            terabyte = storage.getValue() / 1000;

        } else if (storage instanceof MegaByte) {
            terabyte = storage.getValue() / 1000000;

        } else if (storage instanceof KiloByte) {
            terabyte = storage.getValue() / 1000000000;
        }
        return terabyte;
    }

    public static float convertStorage2Gigabyte(StorageUnit storage) {
        float gigabyte = 0;
        if (storage instanceof TeraByte) {
            gigabyte = storage.getValue() * 1000;
        } else if (storage instanceof GigaByte) {
            gigabyte = storage.getValue();

        } else if (storage instanceof MegaByte) {
            gigabyte = storage.getValue() / 1000;

        } else if (storage instanceof KiloByte) {
            gigabyte = storage.getValue() / 1000000;
        }
        return gigabyte;
    }

    public static float convertStorage2Megabyte(StorageUnit storage) {
        float megabyte = 0;
        if (storage instanceof TeraByte) {
            megabyte = storage.getValue() * 1000000;
        } else if (storage instanceof GigaByte) {
            megabyte = storage.getValue() * 1000;

        } else if (storage instanceof MegaByte) {
            megabyte = storage.getValue();

        } else if (storage instanceof KiloByte) {
            megabyte = storage.getValue() / 1000;
        }
        return megabyte;
    }

    public static float convertStorage2Kilobyte(StorageUnit storage) {
        float kilobyte = 0;
        if (storage instanceof TeraByte) {
            kilobyte = storage.getValue() * 1000000000;
        } else if (storage instanceof GigaByte) {
            kilobyte = storage.getValue() * 1000000;

        } else if (storage instanceof MegaByte) {
            kilobyte = storage.getValue() * 1000;

        } else if (storage instanceof KiloByte) {
            kilobyte = storage.getValue();
        }
        return kilobyte;
    }

    public static float convertSpeed2GigaHertz(ComputingUnit speed) {
        float gigahertz = 0;
        if (speed instanceof GigaHertz) {
            gigahertz = speed.getValue();
        } else if (speed instanceof MegaHertz) {
            gigahertz = speed.getValue() / 1000;

        } else if (speed instanceof KiloHertz) {
            gigahertz = speed.getValue() / 1000000;
        }
        return gigahertz;
    }

    public static float convertSpeed2MegaHertz(ComputingUnit speed) {
        float megahertz = 0;
        if (speed instanceof GigaHertz) {
            megahertz = speed.getValue() * 1000;
        } else if (speed instanceof MegaHertz) {
            megahertz = speed.getValue();
        } else if (speed instanceof KiloHertz) {
            megahertz = speed.getValue() / 1000;
        }
        return megahertz;
    }

    public static float convertSpeed2KiloHertz(ComputingUnit speed) {
        float kilohertz = 0;
        if (speed instanceof GigaHertz) {
            kilohertz = speed.getValue() * 1000000;
        } else if (speed instanceof MegaHertz) {
            kilohertz = speed.getValue()*1000;
        } else if (speed instanceof KiloHertz) {
            kilohertz = speed.getValue();
        }
        return kilohertz;
    }
}