package com.stupidtree.hitax.plugin

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.stupidtree.hita.api.HostEnvironmentService
import com.stupidtree.hita.api.Plugin
import com.stupidtree.hita.api.PluginContext
import com.stupidtree.hita.api.UserProfileService
import com.stupidtree.hita.api.UserProfileSnapshot

/** T1 local user session: reads the legacy user preference file; no UI. */
class UserCorePlugin : Plugin {
    override val id = "com.stupidtree.hitax.user"
    override val displayName = "个人主页核心"
    override val requiresApi = 11

    private var application: Application? = null

    override fun install(ctx: PluginContext) {
        val app = (ctx.service(HostEnvironmentService.KEY)?.applicationContext as? Application)
            ?: error("缺少宿主 applicationContext")
        application = app
        ctx.provide(UserProfileService.KEY, UserProfileImpl(app))
    }

    override fun purgeData() = Unit
}

private class UserProfileImpl(private val application: Application) : UserProfileService {
    private val prefs
        get() = application.getSharedPreferences("local_user_profile", 0)

    override fun current(): LiveData<UserProfileSnapshot?> {
        val id = prefs.getString("id", null)
        val value = if (id.isNullOrBlank()) null else UserProfileSnapshot(
            id = id,
            username = prefs.getString("username", null),
            nickname = prefs.getString("nickname", null),
            signature = prefs.getString("signature", null),
            avatar = prefs.getString("avatar", null),
            school = prefs.getString("school", null),
            studentId = prefs.getString("studentId", null),
        )
        return MutableLiveData(value)
    }

    override fun logout() {
        prefs.edit().clear().apply()
    }
}
