package ink.z31.catbooru.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import co.zsmb.materialdrawerkt.builders.accountHeader
import co.zsmb.materialdrawerkt.builders.drawer
import co.zsmb.materialdrawerkt.draweritems.profile.profile
import co.zsmb.materialdrawerkt.draweritems.profile.profileSetting
import com.mikepenz.iconics.typeface.library.googlematerial.GoogleMaterial
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.Drawer
import ink.z31.catbooru.R

class MainActivity : AppCompatActivity() {

    private lateinit var result: Drawer
    private lateinit var headerResult: AccountHeader
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        result = drawer {
            hasStableIds = true
            savedInstance = savedInstanceState
            showOnFirstLaunch = true

            headerResult = accountHeader {
                savedInstance = savedInstanceState
                translucentStatusBar = true

                profile("Mike Penz", "mikepenz@gmail.com") {
                    iconUrl = "https://avatars3.githubusercontent.com/u/1476232?v=3&s=460"
                    identifier = 100
                }
                profile("Bernat Borras", "alorma@github.com") {
                    iconUrl = "https://avatars3.githubusercontent.com/u/887462?v=3&s=460"
                    identifier = 101
                }
                profile("Max Muster", "max.mustermann@gmail.com") {
                    icon = R.drawable.profile2
                    identifier = 102
                }
                profile("Felix House", "felix.house@gmail.com") {
                    icon = R.drawable.profile3
                    identifier = 103
                }
                profile("Mr. X", "mister.x.super@gmail.com") {
                    icon = R.drawable.profile4
                    identifier = 104
                }
                profile("Batman", "batman@gmail.com") {
                    icon = R.drawable.profile5
                    identifier = 105
                }
                profileSetting("Add account", "Add new GitHub Account") {
                    iicon = GoogleMaterial.Icon.gmd_add
                    identifier = 100_000
                }
                profileSetting("Manage Account") {
                    iicon = GoogleMaterial.Icon.gmd_settings
                    identifier = 100_001
                }
            }
        }
    }
}