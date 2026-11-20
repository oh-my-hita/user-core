# HITA user core (T1)

读取 `local_user_profile` 的本地用户会话，提供 `UserProfileService`；登出会清理本地会话并同步清理 host UID。
登录/资料编辑仍由 `user`/`plugin-user` 特权路径处理，后续迁移。
