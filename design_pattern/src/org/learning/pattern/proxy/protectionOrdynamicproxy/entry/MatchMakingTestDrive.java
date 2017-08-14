package org.learning.pattern.proxy.protectionOrdynamicproxy.entry;

import java.lang.reflect.Proxy;
import java.util.Hashtable;

import org.learning.pattern.proxy.protectionOrdynamicproxy.invokationHandler.NonOwnerInvocationHandler;
import org.learning.pattern.proxy.protectionOrdynamicproxy.invokationHandler.OwnerInvocationHandler;
import org.learning.pattern.proxy.protectionOrdynamicproxy.realSubject.PersonBean;
import org.learning.pattern.proxy.protectionOrdynamicproxy.realSubject.PersonBeanImpl;

public class MatchMakingTestDrive {
	Hashtable datingDB = new Hashtable();
 	
	public static void main(String[] args) {
		MatchMakingTestDrive test = new MatchMakingTestDrive();
		test.drive();
	}
 
	public MatchMakingTestDrive() {
		initializeDatabase();
	}

	public void drive() {
		PersonBean joe = getPersonFromDatabase("Joe Javabean"); 
		PersonBean ownerProxy = getOwnerProxy(joe);
		System.out.println("Name is " + ownerProxy.getName());
		ownerProxy.setInterests("bowling, Go");
		System.out.println("Interests set from owner proxy");
		try {
			ownerProxy.setHotOrNotRating(10);
		} catch (Exception e) {
			System.out.println("Can't set rating from owner proxy");
		}
		System.out.println("Rating is " + ownerProxy.getHotOrNotRating());

		PersonBean nonOwnerProxy = getNonOwnerProxy(joe);
		System.out.println("Name is " + nonOwnerProxy.getName());
		try {
			nonOwnerProxy.setInterests("bowling, Go");
		} catch (Exception e) {
			System.out.println("Can't set interests from non owner proxy");
		}
		nonOwnerProxy.setHotOrNotRating(3);
		System.out.println("Rating set from non owner proxy");
		System.out.println("Rating is " + nonOwnerProxy.getHotOrNotRating());
	}

	PersonBean getOwnerProxy(PersonBean person) {
 		
        return (PersonBean) Proxy.newProxyInstance( 
            	person.getClass().getClassLoader(), //pass the Classloader for the subject (PersonBean)
            	person.getClass().getInterfaces(), //the set of interfaces the proxy needs to implement
                new OwnerInvocationHandler(person)); //the invocation handler
	}

	PersonBean getNonOwnerProxy(PersonBean person) {
		
        return (PersonBean) Proxy.newProxyInstance(
            	person.getClass().getClassLoader(),//pass the Classloader for the subject (PersonBean)
            	person.getClass().getInterfaces(), //the set of interfaces the proxy needs to implement
                new NonOwnerInvocationHandler(person));//the invocation handler
	}

	PersonBean getPersonFromDatabase(String name) {
		return (PersonBean)datingDB.get(name);
	}

	void initializeDatabase() {
		PersonBean joe = new PersonBeanImpl();
		joe.setName("Joe Javabean");
		joe.setInterests("cars, computers, music");
		joe.setHotOrNotRating(7);
		datingDB.put(joe.getName(), joe);

		PersonBean kelly = new PersonBeanImpl();
		kelly.setName("Kelly Klosure");
		kelly.setInterests("ebay, movies, music");
		kelly.setHotOrNotRating(6);
		datingDB.put(kelly.getName(), kelly);
	}
}
