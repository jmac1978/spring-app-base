/*
 * SpringCompleteAutoloadTilesContainerFactory.java    Aug 22 2016, 22:45
 *
 * Copyright 2016 Drunken Dev.
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

package com.drunkendev.web.tiles;

import org.apache.tiles.extras.complete.CompleteAutoloadTilesContainerFactory;
import org.apache.tiles.locale.LocaleResolver;
import org.apache.tiles.preparer.factory.PreparerFactory;
import org.apache.tiles.request.ApplicationContext;
import org.springframework.web.servlet.view.tiles3.SpringBeanPreparerFactory;
import org.springframework.web.servlet.view.tiles3.SpringLocaleResolver;


/**
 * Extension to {@link CompleteAutoloadTilesContainerFactory} that provides a spring
 * beans preparer factory and spring locale support.
 *
 * @author  Brett Ryan
 * @since   1.0
 */
public class SpringCompleteAutoloadTilesContainerFactory extends CompleteAutoloadTilesContainerFactory {

    @Override
    protected PreparerFactory createPreparerFactory(ApplicationContext context) {
        return new SpringBeanPreparerFactory();
    }

    @Override
    protected LocaleResolver createLocaleResolver(ApplicationContext applicationContext) {
        return new SpringLocaleResolver();
    }

}
