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


package eu.cloud4soa.adapter.rest.util;

import java.util.Date;

public class Timer {

	private Date start;
	private long difference = 0;
	
	public static Timer tic(){
		Timer timer = new Timer();
		timer.start = new Date();
		return timer;
	}
	
	public long toc(){
		Date end = new Date();
		difference = end.getTime() - start.getTime();
		
		return difference;
	}
	
	public long getDifference(){
		return difference;
	}
}
