package jp.rough_diamond.framework.cache

import org.scalatest.Spec
import jp.rough_diamond.commons.di.DIContainerFactory
import jp.rough_diamond.commons.di.AbstractDIContainer

class SimpleObjectBankSpec extends Spec {
	describe("キーが存在しない場合（scopeKey無し）") {
		val bank = new SimpleObjectBank
		val key = "not exists key!"
		it("getでNoneが返却される事") {
			assert(None == bank.get(key))
		}
		it("containsでfalseが返却される事") {
			assert(false == bank.contains(key))
		}
	}
	
	describe("キーが存在する場合（scopeKey無し）") {
		val bank = new SimpleObjectBank
		val key = "exists key"
		bank.put(key, "Hello World.")
		it("getで登録した時の情報が返却される事") {
			assert("Hello World." == bank.get(key).get);
		}
		it("containsでtrueが返却される事") {
			assert(true == bank.contains(key))
		}
		it("同じキーで異なる情報を登録した場合は後勝ちである事") {
			bank.put(key, "How are you?");
			assert("How are you?" == bank.get(key).get);
		}
		it("削除できる事") {
		  assert(false == bank.remove(key).contains(key))
		}
	}
	
	describe("キーが存在しない場合（scopeKey有り）") {
		val bank = new SimpleObjectBank
		val key = "not exists key!"
		val scope1 = new String("abc")
		val scope2 = new String("abc")
		bank.put(key, "Hello World.(no scope)")
		bank.put(scope2, key, "Hello World.")
		it("getでNoneが返却される事") {
			assert(None == bank.get(scope1, key))
		}
		it("containsでfalseが返却される事") {
			assert(false == bank.contains(scope1, key))
		}
	}

	describe("キーが存在する場合（scopeKey有り）") {
		val bank = new SimpleObjectBank
		val key = "exists key"
		val scope1 = new AnyRef
		val scope2 = new AnyRef
		bank.put(key, "Hello World.(no scope)")
		bank.put(scope1, key, "Hello World.")
		bank.put(scope2, key, "Hello World.(other scope)")
		it("getで登録した時の情報が返却される事") {
			assert("Hello World." == bank.get(scope1, key).get);
		}
		it("containsでtrueが返却される事") {
			assert(true == bank.contains(scope1, key))
		}
		it("同じキーで異なる情報を登録した場合は後勝ちである事") {
			bank.put(scope1, key, "How are you?");
			assert("How are you?" == bank.get(scope1, key).get);
		}
		it("削除できる事") {
		  assert(false == bank.remove(scope1, key).contains(scope1, key))
		}
	}
	
	describe("DIContainerにキャッシャーが指定されていない場合") {
		//厳密には通常のDIContainerを退避しておいてテスト完了後に戻す必要があるが、面倒なので無視
	    val di = new AbstractDIContainer {
	        def getObject[T](cl:Class[T], key:Object) : T =  {
	    	    return null.asInstanceOf[T]
	        }
	        def getSource[T](cl:Class[T]) : T = null.asInstanceOf[T]
	    }
	    DIContainerFactory.setDIContainer(di)
	    it("nullが返却されない事") {
	    	assert(ObjectBank.get != null)
	    }
	}
	
	describe("DIContainerにキャッシャーが指定されてる場合") {
		//厳密には通常のDIContainerを退避しておいてテスト完了後に戻す必要があるが、面倒なので無視
		val bank = new SimpleObjectBank
	    val di = new AbstractDIContainer {
	        def getObject[T](cl:Class[T], key:Object) : T =  {
	            if(key == ObjectBank.DI_KEY) {
	        	    bank.asInstanceOf[T]
	            } else {
	                null.asInstanceOf[T]
	            }
	        }
	        def getSource[T](cl:Class[T]) : T = null.asInstanceOf[T]
	    }
	    DIContainerFactory.setDIContainer(di)
	    it("指定したオブジェクトが返却される事") {
	    	assert(ObjectBank.get eq bank)
	    }
	}
}