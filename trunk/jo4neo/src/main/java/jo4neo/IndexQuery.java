package jo4neo;

import jo4neo.fluent.Is;
import jo4neo.fluent.Result;


class IndexQuery<T> implements Is<T> {

	PersistenceManager pm;
	Class<T> c;
	FieldContext f;
	
	public IndexQuery(FieldContext f, PersistenceManager pm, Class<T> c) {
		this.pm = pm;
		this.c = c;
		this.f = f;
	}
	
	public Result<T> is(Object o) {
		return new ResultImpl<T>(pm,c,f.getIndexName(), o);
	}

}

/**
 * Copyright (C) 2009
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */