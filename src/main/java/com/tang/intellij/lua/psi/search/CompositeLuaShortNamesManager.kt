/*
 * Copyright (c) 2017. tangzx(love.tangzx@qq.com)
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

package com.tang.intellij.lua.psi.search

import com.intellij.openapi.extensions.ExtensionPointName
import com.intellij.openapi.project.Project
import com.intellij.util.Processor
import com.tang.intellij.lua.psi.LuaClass
import com.tang.intellij.lua.psi.LuaClassMember
import com.tang.intellij.lua.psi.LuaTypeAlias
import com.tang.intellij.lua.search.SearchContext
import com.tang.intellij.lua.ty.ITy
import com.tang.intellij.lua.ty.ITyClass

class CompositeLuaShortNamesManager : LuaShortNamesManager {
    override fun findAlias(name: String, context: SearchContext): LuaTypeAlias? {
        for (manager in EP_NAME.extensionList) {
            val alias = manager.findAlias(name, context)
            if (alias != null)
                return alias
        }
        return null
    }

    override fun findClass(name: String, context: SearchContext): LuaClass? {
        for (manager in EP_NAME.extensionList) {
            val c = manager.findClass(name, context)
            if (c != null)
                return c
        }
        return null
    }

    override fun findMember(type: ITyClass, fieldName: String, context: SearchContext): LuaClassMember? {
        for (manager in EP_NAME.extensionList) {
            val ret = manager.findMember(type, fieldName, context)
            if (ret != null) return ret
        }
        return null
    }

    override fun findIndexer(type: ITyClass, indexTy: ITy, context: SearchContext, exact: Boolean): LuaClassMember? {
        for (manager in EP_NAME.extensionList) {
            val ret = manager.findIndexer(type, indexTy, context, exact)
            if (ret != null) return ret
        }
        return null
    }

    override fun processAllAliases(project: Project, processor: Processor<String>): Boolean {
        for (manager in EP_NAME.extensionList) {
            if (!manager.processAllAliases(project, processor))
                return false
        }
        return true
    }

    override fun processAliases(name: String, context: SearchContext, processor: Processor<in LuaTypeAlias>): Boolean {
        for (manager in EP_NAME.extensionList) {
            if (!manager.processAliases(name, context, processor))
                return false
        }
        return true
    }

    override fun processAllClasses(project: Project, processor: Processor<String>): Boolean {
        for (manager in EP_NAME.extensionList) {
            if (!manager.processAllClasses(project, processor))
                return false
        }
        return true
    }

    override fun processClasses(name: String, context: SearchContext, processor: Processor<in LuaClass>): Boolean {
        for (manager in EP_NAME.extensionList) {
            if (!manager.processClasses(name, context, processor))
                return false
        }
        return true
    }

    override fun getClassMembers(clazzName: String, context: SearchContext): Collection<LuaClassMember> {
        val collection = mutableListOf<LuaClassMember>()
        for (manager in EP_NAME.extensionList) {
            val col = manager.getClassMembers(clazzName, context)
            collection.addAll(col)
        }
        return collection
    }

    override fun processMember(type: ITyClass, fieldName: String, context: SearchContext, processor: Processor<in LuaClassMember>): Boolean {
        for (manager in EP_NAME.extensionList) {
            if (!manager.processMember(type, fieldName, context, processor))
                return false
        }
        return true
    }

    override fun processIndexer(type: ITyClass, indexTy: ITy, exact: Boolean, context: SearchContext, processor: Processor<in LuaClassMember>): Boolean {
        for (manager in EP_NAME.extensionList) {
            if (!manager.processIndexer(type, indexTy, false, context, processor))
                return false
        }
        return true
    }

    companion object {
        private val EP_NAME = ExtensionPointName.create<LuaShortNamesManager>("au.com.glassechidna.luanalysis.luaShortNamesManager")
    }
}
