package org.breizhjug.breizhlib.utils;

/*
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors:
 *     ybonnel - initial API and implementation
 */

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import org.breizhjug.breizhlib.BreizhLibConstantes;
import org.breizhjug.breizhlib.exception.BreizhLibException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class Version {

    private final static String URL_VERSION = BreizhLibConstantes.SERVER_URL + "android/version";

    private static int versionCodeMarket = 0;

    private static String versionMarket;

    public static String getVersionMarket() {
        return versionMarket;
    }

    public static int getVersionCodeMarket() {
        try {
            URL urlVersion = new URL(URL_VERSION);
            URLConnection connection = urlVersion.openConnection();
            connection.setConnectTimeout(30000);
            connection.setReadTimeout(30000);
            BufferedReader bufReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            versionCodeMarket = Integer.valueOf(bufReader.readLine());
            versionMarket = bufReader.readLine();
            bufReader.close();
        } catch (Exception ignore) {
        }
        return versionCodeMarket;
    }

    private static int versionCode = 0;

    public static String version = "0";

    public static int getVersionCourante(Application application) {
        if (versionCode == 0) {
            PackageManager manager = application.getPackageManager();
            try {
                PackageInfo info = manager.getPackageInfo(application.getPackageName(), 0);
                versionCode = info.versionCode;
                version = info.versionName;
            } catch (PackageManager.NameNotFoundException exception) {
                throw new BreizhLibException(exception);
            }
        }
        return versionCode;
    }

}