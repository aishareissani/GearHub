package org.umn.gearhub.data.remote

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import org.umn.gearhub.BuildConfig

val supabase: SupabaseClient by lazy {
    createSupabaseClient(BuildConfig.SUPABASE_URL, BuildConfig.SUPABASE_KEY) {
        install(Auth)
        install(Postgrest)
    }
}
