/*
 * SpringCompleteAutoloadTilesInitializer.java    Aug 22 2016, 22:47
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

import org.apache.tiles.extras.complete.CompleteAutoloadTilesInitializer;
import org.apache.tiles.factory.AbstractTilesContainerFactory;
import org.apache.tiles.request.ApplicationContext;



/**
 *
 * @author  Brett Ryan
 */
public class SpringCompleteAutoloadTilesInitializer extends CompleteAutoloadTilesInitializer {

    @Override
    protected AbstractTilesContainerFactory createContainerFactory(ApplicationContext context) {
        return new SpringCompleteAutoloadTilesContainerFactory();
    }

}
