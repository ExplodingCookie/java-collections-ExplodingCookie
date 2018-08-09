package com.cooksys.ftd.assignments.collections;

import com.cooksys.ftd.assignments.collections.hierarchy.Hierarchy;
import com.cooksys.ftd.assignments.collections.model.Capitalist;
import com.cooksys.ftd.assignments.collections.model.FatCat;

//import sun.reflect.generics.reflectiveObjects.NotImplementedException; 

import java.util.*;

public class MegaCorp implements Hierarchy<Capitalist, FatCat> {
	
	private Map<FatCat, Set<Capitalist>> hierarchy = new HashMap<FatCat, Set<Capitalist>>();

    /**
     * Adds a given element to the hierarchy.
     * <p>
     * If the given element is already present in the hierarchy,
     * do not add it and return false
     * <p>
     * If the given element has a parent and the parent is not part of the hierarchy,
     * add the parent and then add the given element
     * <p>
     * If the given element has no parent but is a Parent itself,
     * add it to the hierarchy
     * <p>
     * If the given element has no parent and is not a Parent itself,
     * do not add it and return false
     *
     * @param capitalist the element to add to the hierarchy
     * @return true if the element was added successfully, false otherwise
     */
    @Override
    public boolean add(Capitalist capitalist) {
    	if(capitalist == null) {
    		return false;
    	} else {
    		if(!has(capitalist)) {
    			boolean added = false;
    			
    			if(capitalist instanceof FatCat) {
    				hierarchy.put((FatCat) capitalist, new HashSet<Capitalist>());
    				added = true;
    			}
    			
    			if(capitalist.hasParent()) {
    				FatCat target = capitalist.getParent();
    				
    				if(!hierarchy.containsKey(target)) {
						hierarchy.put(target, new HashSet<Capitalist>());
					}
    				
    				while(target.hasParent()) {
    					if(!hierarchy.containsKey(target.getParent())) {
    						hierarchy.put(target.getParent(), new HashSet<Capitalist>());
    					}
    					
    					target = target.getParent();
    				}
    				
    				Set<Capitalist> baseSet = hierarchy.get(capitalist.getParent());
    				
    				if(!baseSet.contains(capitalist)) {
	    				Set<Capitalist> newSet = new HashSet<Capitalist>();
	    				newSet.addAll(baseSet);
	    				newSet.add(capitalist);
	    				
	    				hierarchy.replace(capitalist.getParent(), baseSet, newSet);
	    				
	    				added = true;
    				}
    			}
    			
    			return added;
    		} else {
    			return false;
    		}
    	}
    }

    /**
     * @param capitalist the element to search for
     * @return true if the element has been added to the hierarchy, false otherwise
     */
    @Override
    public boolean has(Capitalist capitalist) {
    	if(capitalist != null) {
	    	if(capitalist instanceof FatCat) {
	    		if(hierarchy.containsKey(capitalist)) {
	    			return true;
	    		} else {
	    			return false;
	    		}
	    	} else {
	    		if(capitalist.hasParent()) {
	    			if(hierarchy.containsKey(capitalist.getParent())) {
	    				if(hierarchy.get(capitalist.getParent()).contains(capitalist)) {
	    					return true;
	    				} else {
	    					return false;
	    				}
	    			} else {
	    				return false;
	    			}
	    		} else {
	    			return false;
	    		}
	    	}
    	} else {
    		return false;
    	}
    }

    /**
     * @return all elements in the hierarchy,
     * or an empty set if no elements have been added to the hierarchy
     */
    @Override
    public Set<Capitalist> getElements() {    	
    	Set<Capitalist> collection = new HashSet<Capitalist>();
    	
    	Set<FatCat> parents = getParents();
    	
    	for(FatCat c : parents) {
    		collection.add(c);
    		Set<Capitalist> children = getChildren(c);
    		collection.addAll(children);
    	}
    	
    	return collection;
    }

    /**
     * @return all parent elements in the hierarchy,
     * or an empty set if no parents have been added to the hierarchy
     */
    @Override
    public Set<FatCat> getParents() {
    	Set<FatCat> returnSet = new HashSet<FatCat>();
    	
    	for(FatCat c : hierarchy.keySet()) {
    		if(!returnSet.contains(c)) {
    			returnSet.add(c);
    		}
    	}
    	
    	return returnSet;
    }

    /**
     * @param fatCat the parent whose children need to be returned
     * @return all elements in the hierarchy that have the given parent as a direct parent,
     * or an empty set if the parent is not present in the hierarchy or if there are no children
     * for the given parent
     */
    @Override
    public Set<Capitalist> getChildren(FatCat fatCat) {	
    	Set<Capitalist> children = new HashSet<Capitalist>();
    	
    	if(hierarchy.containsKey(fatCat)) {
    		Set<Capitalist> stuff = new HashSet<Capitalist>();
    		stuff.addAll(hierarchy.get(fatCat));
    		
    		children = stuff;
    	}
    	
    	return children;
    }

    /**
     * @return a map in which the keys represent the parent elements in the hierarchy,
     * and the each value is a set of the direct children of the associate parent, or an
     * empty map if the hierarchy is empty.
     */
    @Override
    public Map<FatCat, Set<Capitalist>> getHierarchy() {
    	Map<FatCat, Set<Capitalist>> map = new HashMap<FatCat, Set<Capitalist>>();
    	
    	for(FatCat c : hierarchy.keySet()) {
    		Set<Capitalist> stuff = new HashSet<Capitalist>();
    		stuff.addAll(hierarchy.get(c));
    		
    		map.put(c, stuff);
    	}
    	
    	return map;
    }

    /**
     * @param capitalist
     * @return the parent chain of the given element, starting with its direct parent,
     * then its parent's parent, etc, or an empty list if the given element has no parent
     * or if its parent is not in the hierarchy
     */
    @Override
    public List<FatCat> getParentChain(Capitalist capitalist) {

    	List<FatCat> cats = new LinkedList<FatCat>();
    	
    	if(capitalist != null && hierarchy.containsKey(capitalist.getParent())) {
    		
	    	FatCat parent = capitalist.getParent();
	    	
	    	while(parent != null) {
	    		cats.add(parent);
	    		parent = parent.getParent();
	    	}
    	}
    	
    	
    	return cats;
    }
}
