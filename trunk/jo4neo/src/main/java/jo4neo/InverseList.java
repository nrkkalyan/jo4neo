package jo4neo;

import java.util.Collection;
import java.util.Iterator;

import jo4neo.util.Lazy;

/**
 * Represents implied relationships.
 * members cannot be added or removed as this
 * is contingent upon relationships declared from another
 * entity.
 *
 */
@SuppressWarnings("unchecked")
public class InverseList implements Lazy {

	private transient FieldContext field;
	private transient LoadOperation loader;
	private Collection data;

	
	public InverseList(FieldContext f, LoadOperation neo) {
		field = f;
		this.loader = neo;
	}

	private Collection data() {
		if ( data == null)
			data = loader.loadInverse(field);
		return data;
	}
	
	public boolean add(Object e) {
		return false;
	}

	public boolean addAll(Collection c) {
		return false;
	}


	public void clear() {

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
		return false;
	}

	public boolean removeAll(Collection c) {
		return false;
	}

	public boolean retainAll(Collection c) {
		return false;
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
		return false;
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

