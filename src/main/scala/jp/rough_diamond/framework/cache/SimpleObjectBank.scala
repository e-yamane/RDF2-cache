package jp.rough_diamond.framework.cache
import scala.collection.mutable.HashMap
import java.util.IdentityHashMap
import scala.collection.mutable.Map

class SimpleObjectBank extends ObjectBank {
	private lazy val map = new HashMap[String, AnyRef]
	private lazy val mapWithScope = new IdentityHashMap[AnyRef, Map[String, AnyRef]] {
		override def get(key:AnyRef) = {
			if(super.get(key) == null) {
				put(key, new HashMap[String, AnyRef]);
				get(key);
			} else {
				super.get(key)
			}
		}
	}

	def get(key: String): Option[AnyRef] = { 
	    map.get(key)
	}

	def put(key: String, target: Object): ObjectBank = { 
	    map += (key -> target)
	    this
	}

	def contains(key:String) : Boolean = map.contains(key)

	def remove(key:String) : ObjectBank = {
		map -= key
		this
	}

	def get(scopeKey:AnyRef, key:String) : Option[AnyRef] = {
		mapWithScope.get(scopeKey).get(key)
	} 
	
	def put(scopeKey:AnyRef, key:String, target:AnyRef) : ObjectBank = {
		mapWithScope.get(scopeKey) += (key -> target)
		this
	}
	
	def contains(scopeKey:AnyRef, key:String) : Boolean = {
		mapWithScope.get(scopeKey).contains(key)
	}
	
	def remove(scopeKey:AnyRef, key:String) : ObjectBank = {
		mapWithScope.get(scopeKey) -= key
		this
	}
}