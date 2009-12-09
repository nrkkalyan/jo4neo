package jo4neo;

import java.lang.reflect.Field;

import org.neo4j.api.core.DynamicRelationshipType;
import org.neo4j.api.core.RelationshipType;

public class RelationFactoryImpl implements RelationFactory {

	public RelationshipType relationshipType(Field f) {		
		if ( f.isAnnotationPresent(neo.class)) {
			neo p = f.getAnnotation(neo.class);
			String name = p.value();
			if (!neo.DEFAULT.equals(name))
				return relationshipType(name);
			
		}
		String n = f.getName();
		return relationshipType(n);
			
	}

	public RelationshipType relationshipType(String name) {
		return DynamicRelationshipType.withName(name);
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