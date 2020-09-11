# Datomic Blog Kata

A Clojure kata designed to lear Datomic. Try to do it step by step.

## Usage
Use the Datomic in-memory storage.
Create a schema for the blog posts with title and body.

### Step 1
Write a function that can retrieve a blog post key by title. Assume that title is unique.

### Step 2
Write a function that can retrieve all blog post keys with a given string in the title.

### Step 3
Write a function that can retrieve a blog post entity by title.

### Step 4
Write a function that can update a blog post content.

### Step 5
Write a function that can retrieve the history of a blog post.

### Step 6
Add the schema authors with the name and age.
Add the association between posts and authors (a post has only one author).
Write a function that can retrieve a blog post entity by title with his author.

### Step 7
Write a function that can retrieve all blog post entities of authors older than a given age.

### Step 8
Change the function at Step 7 in order to sort the posts by the author's age (younger first).

## Author
[Marco Fracassi](https://github.com/marco-fracassi-milkman)

## License

Copyright © 2020 FIXME

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
