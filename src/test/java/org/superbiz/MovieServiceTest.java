/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.superbiz;

import org.apache.cxf.feature.LoggingFeature;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.johnzon.jaxrs.JohnzonProvider;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.core.Response;
import java.net.URL;

import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class MovieServiceTest {

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
                .addPackages(true, Api.class.getPackage())
                .addAsResource("META-INF/microprofile-config.properties");
    }

    @ArquillianResource
    private URL base;

    @Test
    public void testAsEveryone() throws Exception {
        final WebClient webClient = createWebClient(base);

        final Movie movie = new Movie(1, "The Matrix", "Lana Wachowski");

        final String jwt = "eyJraWQiOiI3M1dmMFBMVFpLUEZFWWQ1X1hLY0JKcHc0VUh1M0FXNWpSY2tiam1JZ1RVIiwiYWxnIjoiUlMyNTYifQ.eyJ2ZXIiOjEsImp0aSI6IkFULi1sWjZqZkY2U3N5c0tDRXJFVXFLWUxIYjhuaS1mUkdpdHlUUnI0ZjF0RWsiLCJpc3MiOiJodHRwczovL2Rldi0xMzMzMjAub2t0YS5jb20vb2F1dGgyL2RlZmF1bHQiLCJhdWQiOiJhcGk6Ly9kZWZhdWx0IiwiaWF0IjoxNTYyNzk5ODIxLCJleHAiOjE1NjI4MDM0MjEsImNpZCI6IjBvYXVwMmc0ZWlBYmNWU3FpMzU2IiwidWlkIjoiMDB1dmw1MHpqVWt3UXNGWHAzNTYiLCJzY3AiOlsib3BlbmlkIl0sInN1YiI6ImRibGV2aW5zQHRvbWl0cmliZS5jb20iLCJ1cG4iOiJkYmxldmluc0B0b21pdHJpYmUuY29tIiwiZ3JvdXBzIjpbIkV2ZXJ5b25lIl19.bsO6ppwATRWvxJOGnGRvoae6MzdThMajirT2u4CEjG5_U4cpeBc98xIUDExGr-c32km5cESU6qVbsrgCyRvyGg9dqVdOHWxlDYM1w6A3T0RlhhfFlzIC8HW1KzY4MHP7TFxURUkfnup_XtmWQfhMbvHZJVaDQkD5ZpBFcgOdfVFX2rkFpTRAJdBYELFUGh0ddqFoIXkDCtW0GFetNnfHGL2_YjpI52ZbfSPY0AV1C8f461MkMUnCXRLnGE0DfX2S_KGtGTaR1gPYPretg9j8zfeNUhk5RktjyMeJ9N6JzrUVotVo-SbpOOFHQDEB6wR38l-3GcUyYNztUAuP9ZXh_w";

        final Response response = webClient.reset()
                .path("/api/movies")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + jwt)
                .post(movie);
        assertEquals(204, response.getStatus());

    }

    @Test
    public void testInvalidToken() throws Exception {
        final WebClient webClient = createWebClient(base);

        final Movie movie = new Movie(1, "The Matrix", "Lana Wachowski");

        final String jwt = "eyJraWQiOiI3M1dmMFBMVFpLUEZFWWQ1X1hLY0JKcHc0VUh1M0FXNWpSY2tiam1JZ1RVIiwiYWxnIjoiUlMyNTYifQ.eyJ2ZXIiOjEsImp0aSI6IkFULi1sWjZqZkY2U3N5c0tDRXJFVXFLWUxIYjhuaS1mUkdpdHlUUnI0ZjF0RWsiLCJpc3MiOiJodHRwczovL2Rldi0xMzMzMjAub2t0YS5jb20vb2F1dGgyL2RlZmF1bHQiLCJhdWQiOiJhcGk6Ly9kZWZhdWx0IiwiaWF0IjoxNTYyNzk5ODIxLCJleHAiOjE1NjI4MDM0MjEsImNpZCI6IjBvYXVwMmc0ZWlBYmNWU3FpMzU2IiwidWlkIjoiMDB1dmw1MHpqVWt3UXNGWHAzNTYiLCJzY3AiOlsib3BlbmlkIl0sInN1YiI6ImRibGV2aW5zQHRvbWl0cmliZS5jb20iLCJ1cG4iOiJkYmxldmluc0B0b21pdHJpYmUuY29tIiwiZ3JvdXBzIjpbIkV2ZXJ5b25lIl19.bsO6ppwATRWvxJOGnGRvoae6MzdThMajirT2u4CEjG5_U4cpeBc98xIUDExGr-c32km5cESU6qVbsrgCyRvyGg9dqVdOHWxlDYM1w6A3T0RlhhfFlzIC8HW1KzY4MHP7TFxURUkfnup_XtmWQfhMbvHZJVaDQkD5ZpBFcgOdfVFX2rkFpTRAJdBYELFUGh0ddqFoIXkDCtW0GFetNnfHGL2_YjpI52ZbfSPY0AV1C8f461MkMUnCXRLnGE0DfX2S_KGtGTaR1gPYPretg9j8zfeNUhk5RktjyMeJ9N6JzrUVotVo";

        final Response response = webClient.reset()
                .path("/api/movies")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + jwt)
                .post(movie);
        assertEquals(401, response.getStatus());

    }

    /**
     * The JWT has "Everyone" permission and cannot delete all the movies
     */
    @Test
    public void testManagerRequired() throws Exception {
        final WebClient webClient = createWebClient(base);

        final String jwt = "eyJraWQiOiI3M1dmMFBMVFpLUEZFWWQ1X1hLY0JKcHc0VUh1M0FXNWpSY2tiam1JZ1RVIiwiYWxnIjoiUlMyNTYifQ.eyJ2ZXIiOjEsImp0aSI6IkFULi1sWjZqZkY2U3N5c0tDRXJFVXFLWUxIYjhuaS1mUkdpdHlUUnI0ZjF0RWsiLCJpc3MiOiJodHRwczovL2Rldi0xMzMzMjAub2t0YS5jb20vb2F1dGgyL2RlZmF1bHQiLCJhdWQiOiJhcGk6Ly9kZWZhdWx0IiwiaWF0IjoxNTYyNzk5ODIxLCJleHAiOjE1NjI4MDM0MjEsImNpZCI6IjBvYXVwMmc0ZWlBYmNWU3FpMzU2IiwidWlkIjoiMDB1dmw1MHpqVWt3UXNGWHAzNTYiLCJzY3AiOlsib3BlbmlkIl0sInN1YiI6ImRibGV2aW5zQHRvbWl0cmliZS5jb20iLCJ1cG4iOiJkYmxldmluc0B0b21pdHJpYmUuY29tIiwiZ3JvdXBzIjpbIkV2ZXJ5b25lIl19.bsO6ppwATRWvxJOGnGRvoae6MzdThMajirT2u4CEjG5_U4cpeBc98xIUDExGr-c32km5cESU6qVbsrgCyRvyGg9dqVdOHWxlDYM1w6A3T0RlhhfFlzIC8HW1KzY4MHP7TFxURUkfnup_XtmWQfhMbvHZJVaDQkD5ZpBFcgOdfVFX2rkFpTRAJdBYELFUGh0ddqFoIXkDCtW0GFetNnfHGL2_YjpI52ZbfSPY0AV1C8f461MkMUnCXRLnGE0DfX2S_KGtGTaR1gPYPretg9j8zfeNUhk5RktjyMeJ9N6JzrUVotVo-SbpOOFHQDEB6wR38l-3GcUyYNztUAuP9ZXh_w";

        final Response response = webClient.reset()
                .path("/api/movies")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + jwt)
                .delete();
        assertEquals(403, response.getStatus());
    }

    @Test
    public void testAsAnonymous() throws Exception {
        final WebClient webClient = createWebClient(base);

        // Should return a 401 since the POST request lacks the Authorization header
        final Response response = webClient.reset()
                .path("/api/movies")
                .header("Content-Type", "application/json")
                .post(new Movie());
        assertEquals(401, response.getStatus());
    }

    private static WebClient createWebClient(final URL base) {
        return WebClient.create(base.toExternalForm(), singletonList(new JohnzonProvider<>()),
                singletonList(new LoggingFeature()), null);
    }

}
