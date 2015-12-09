/*
 * Copyright (C) 2011 The Android Open Source Project
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
package com.scoutingstuff.paul.ivossenjacht.graphics;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.scoutingstuff.paul.ivossenjacht.gameProcess;
import com.scoutingstuff.paul.ivossenjacht.graphics.DirectionGLRenderer;

/**
 * Created by user on 8/27/15.
 */
public class DirectionGLSurfaceView extends GLSurfaceView {

    private final DirectionGLRenderer mRenderer;

    public DirectionGLSurfaceView(Context context){
        super(context);

        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);

        mRenderer = new DirectionGLRenderer();
        setEGLConfigChooser(8 , 8, 8, 8, 16, 0);
        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(mRenderer);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }
    public gameProcess getGameProcess() {
        return mRenderer.getGameProcess();
    }
}
