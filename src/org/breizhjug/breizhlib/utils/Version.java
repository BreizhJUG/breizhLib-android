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
import org.breizhjug.breizhlib.BreizhLib;
import org.breizhjug.breizhlib.exception.BreizhLibException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class Version {

    private final static String URL_VERSION = BreizhLib.SERVER_URL + "android/version";

    private static String versionMarket = null;

    public static String getVersionMarket() {
        try {
            URL urlVersion = new URL(URL_VERSION);
            URLConnection connection = urlVersion.openConnection();
            connection.setConnectTimeout(30000);
            connection.setReadTimeout(30000);
            BufferedReader bufReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            versionMarket = bufReader.readLine();
            bufReader.close();
        } catch (Exception ignore) {
        }
        return versionMarket;
    }

    private static String version = null;

    public static String getVersionCourante(Application application) {
        if (version == null) {
            PackageManager manager = application.getPackageManager();
            try {
                PackageInfo info = manager.getPackageInfo(application.getPackageName(), 0);
                version = info.versionName;
            } catch (PackageManager.NameNotFoundException exception) {
                throw new BreizhLibException(exception);
            }
        }
        return version;
    }

}