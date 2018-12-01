/*
 * Copyright (c) 2012-2018 Red Hat, Inc.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 */
package org.eclipse.che.api.devfile.server;

import static org.eclipse.che.api.devfile.server.Constants.SCHEMA_LOCATION;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.SoftReference;
import java.util.stream.Collectors;
import javax.inject.Singleton;

/** Loads a schema content and stores it in soft reference. */
@Singleton
public class DevFileSchemaProvider {

  private SoftReference<String> schemaRef = new SoftReference(null);

  public String getSchemaContent() throws IOException {
    String schema = schemaRef.get();
    if (schema == null) {
      schema = loadFile();
      schemaRef = new SoftReference<>(schema);
    }
    return schema;
  }

  public JsonNode getJsoneNode() throws IOException {
    return JsonLoader.fromString(getSchemaContent());
  }

  private String loadFile() throws IOException {
    try (InputStream schemaStream =
        getClass().getClassLoader().getResourceAsStream(SCHEMA_LOCATION)) {
      return new BufferedReader(new InputStreamReader(schemaStream))
          .lines()
          .collect(Collectors.joining("\n"));
    }
  }
}
