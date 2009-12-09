package jo4neo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import jo4neo.util.Lazy;



@SuppressWarnings("unchecked")
class LazyList implements Set<Object>, Lazy {

	private transient FieldContext field;
	private transient LoadOperation loader;
	private transient Collection newdata;

	private Collection data;
	private boolean modified = false;
	
	public LazyList(FieldContext f, LoadOperation neo) {
		field = f;
		this.loader = neo;
	}

	private Collection data() {
		if ( data == null)
			data = loader.load(field);
		return data;
	}
	
	protected Collection newdata() {
		if ( newdata == null)
			newdata = new ArrayList<Object>();
		return newdata;
	}
	
	protected Collection consumeUpdates() {
		Collection updates = newdata();
		newdata = null;
		return updates;
	}

	public boolean add(Object e) {
		modified = true;
		if (data().add(e)) {
			newdata().add(e);
			return true;
		}
		return false;
	}

	public boolean addAll(Collection c) {
		modified = true;
		return data().addAll(c);
	}


	public void clear() {
		modified = true;
		data().clear();
	}

	public boolean contains(Object o) {
		return data().contains(o);
	}

	public boolean containsAll(Collection c) {
		return data().containsAll(c);
	}

	public boolean equals(Object o) {
		return data().equals(o);
	}

	public int hashCode() {
		return data().hashCode();
	}

	public boolean isEmpty() {
		return data().isEmpty();
	}

	public Iterator iterator() {
		return data().iterator();
	}

	public boolean remove(Object o) {
		modified = true;
		loader.removeRelationship(field, o);
		return data().remove(o);
	}

	public boolean removeAll(Collection c) {
		modified = true;
		return data().removeAll(c);
	}

	public boolean retainAll(Collection c) {
		modified = true;
		return data().retainAll(c);
	}
	
	public int size() {
		return data().size();
	}

	public Object[] toArray() {
		return data().toArray();
	}

	public Object[] toArray(Object[] a) {
		return data().toArray(a);
	}

	public boolean isConnected() {
		return data != null;
	}

	public boolean modified() {
		return modified;
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