/*
 * TimingUtils.java    Mar 30 2017, 11:27
 *
 * Copyright 2017 Drunken Dev.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.drunkendev.util;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;


/**
 *
 * @author  Brett Ryan
 */
public class TimingUtils {

    public static Duration time(Runnable task) {
        Instant start = Instant.now();
        task.run();
        return Duration.between(start, Instant.now());
    }

    public static <T> Duration time(Consumer<T> task, T arg) {
        Instant start = Instant.now();
        task.accept(arg);
        return Duration.between(start, Instant.now());
    }

    public static TimingResult<String> time(String name, Consumer<String> task) {
        return new TimingResult<>(name, time(task, name));
    }

    public static <T> TimingResult time(Supplier<T> task) {
        Instant start = Instant.now();
        T res = task.get();
        return new TimingResult<>(res, Duration.between(start, Instant.now()));
    }

    public static <I, R> TimingResult time(Function<I, R> task, I arg) {
        Instant start = Instant.now();
        R res = task.apply(arg);
        return new TimingResult<>(res, Duration.between(start, Instant.now()));
    }

}
