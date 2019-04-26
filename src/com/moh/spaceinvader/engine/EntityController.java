package com.moh.spaceinvader.engine;

import java.util.*;

public class EntityController {

	private final static List<Entity> GAME_ACTORS = new ArrayList<>();
	private final static List<Entity> CHECK_COLLISION_LIST = new ArrayList<>();
	private final static Set<Entity> CLEAN_UP_ENTITIES = new HashSet<>();

	public List<Entity> getAllEntities() {
		return GAME_ACTORS;
	}

	public void addEntities(Entity... entities) {
		GAME_ACTORS.addAll(Arrays.asList(entities));
	}

	public void removeEntities(Entity... entities) {
		GAME_ACTORS.removeAll(Arrays.asList(entities));
	}

	public Set<Entity> getEntitiesToBeRemoved() {
		return CLEAN_UP_ENTITIES;
	}

    public void addEntitiesToBeRemoved(Entity... entities) {
        if (entities.length > 1) {
        	CLEAN_UP_ENTITIES.addAll(Arrays.asList((Entity[]) entities));
        } else {
        	CLEAN_UP_ENTITIES.add(entities[0]);
        }
    }

	public List<Entity> getCollisionsToCheck() {
		return CHECK_COLLISION_LIST;
	}

	public void resetCollisionsToCheck() {
		CHECK_COLLISION_LIST.clear();
		CHECK_COLLISION_LIST.addAll(GAME_ACTORS);
	}

	public void cleanupEntities() {
		GAME_ACTORS.removeAll(CLEAN_UP_ENTITIES);
		CLEAN_UP_ENTITIES.clear();
	}

}
