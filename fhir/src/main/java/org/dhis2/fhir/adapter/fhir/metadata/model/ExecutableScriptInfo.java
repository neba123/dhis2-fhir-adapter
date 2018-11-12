package org.dhis2.fhir.adapter.fhir.metadata.model;

/*
 * Copyright (c) 2004-2018, University of Oslo
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * Neither the name of the HISP project nor the names of its contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import reactor.util.annotation.NonNull;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The executable script prepared to be processed by a script executor.
 *
 * @author volsch
 */
public class ExecutableScriptInfo implements Serializable
{
    private static final long serialVersionUID = 7675659610124741889L;

    private final ExecutableScript executableScript;

    private final List<ExecutableScriptArg> executableScriptArgs;

    private final Script script;

    private final List<ScriptArg> scriptArgs;

    private final ScriptSource scriptSource;

    public ExecutableScriptInfo( @JsonProperty( "executableScript" ) @NonNull ExecutableScript executableScript, @JsonProperty( "executableScriptArgs" ) @Nonnull List<ExecutableScriptArg> executableScriptArgs,
        @JsonProperty( "script" ) @Nonnull Script script, @JsonProperty( "scriptArgs" ) @Nonnull List<ScriptArg> scriptArgs,
        @JsonProperty( "scriptSource" ) @Nonnull ScriptSource scriptSource )
    {
        this.executableScript = executableScript;
        // do not use persistence specific list implementations when serializing to JSON
        this.executableScriptArgs = new ArrayList<>( executableScriptArgs );
        this.script = script;
        // do not use persistence specific list implementations when serializing to JSON
        this.scriptArgs = new ArrayList<>( scriptArgs );
        this.scriptSource = scriptSource;
    }

    @Nonnull
    public ExecutableScript getExecutableScript()
    {
        return executableScript;
    }

    @Nonnull
    public Script getScript()
    {
        return script;
    }

    @Nonnull
    public List<ExecutableScriptArg> getExecutableScriptArgs()
    {
        return executableScriptArgs;
    }

    @Nonnull
    public List<ScriptArg> getScriptArgs()
    {
        return scriptArgs;
    }

    @Nonnull
    public ScriptSource getScriptSource()
    {
        return scriptSource;
    }
}
