package jp.rough_diamond.framework.cache
import java.util.List
import jp.rough_diamond.commons.di.DIContainerFactory

abstract class ObjectBank {
	def get(key:String) : Option[AnyRef]
	def put(key:String, target:AnyRef) : ObjectBank
	def contains(key:String) : Boolean
	def remove(key:String) : ObjectBank

	def get(scopeKey:AnyRef, key:String) : Option[AnyRef]
	def put(scopeKey:AnyRef, key:String, target:AnyRef) : ObjectBank
	def contains(scopeKey:AnyRef, key:String) : Boolean
	def remove(scopeKey:AnyRef, key:String) : ObjectBank
}

object ObjectBank {
    val DI_KEY = "CACHER";
    private lazy val default = new SimpleObjectBank
    def get : ObjectBank = {
    	val tmp = DIContainerFactory.getDIContainer().getObject(DI_KEY)
        if(tmp == null) {
            default  
        } else {
            tmp.asInstanceOf[ObjectBank]
        }
    }
}