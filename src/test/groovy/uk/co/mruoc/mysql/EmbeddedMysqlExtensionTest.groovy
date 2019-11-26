package uk.co.mruoc.mysql

import org.junit.Test

import static com.wix.mysql.distribution.Version.v5_6_latest
import static com.wix.mysql.distribution.Version.v8_latest
import static org.assertj.core.api.Assertions.assertThat
import static org.assertj.core.api.Assertions.catchThrowable

class EmbeddedMysqlExtensionTest {

    private static final def DATABASE_NAME = "databaseName"
    private static final def OVERRIDE_PORT = 3307
    private static final def URL = "jdbc:mysql://localhost:" + OVERRIDE_PORT + "/" + DATABASE_NAME

    private static final def DEFAULT_USERNAME = "user"
    private static final def DEFAULT_VERSION = v8_latest
    private static final def DEFAULT_PORT = 3306
    private static final def DEFAULT_TIMEOUT_SECONDS = 30
    private static final def DEFAULT_TEMP_DIR = 'build/mysql-temp/'

    private static final def OVERRIDE_USERNAME = "anotherUser"
    private static final def OVERRIDE_VERSION = v5_6_latest
    private static final def OVERRIDE_TIMEOUT_SECONDS = 60
    private static final def OVERRIDE_TEMP_DIR = 'build/mysql-custom-temp'

    private static final def PASSWORD = "password"

    private def extension = new EmbeddedMysqlExtension()

    @Test
    void usernameDefaultToUser() {
        assertThat(extension.username).isEqualTo(DEFAULT_USERNAME)
    }

    @Test
    void passwordShouldDefaultToEmpty() {
        assertThat(extension.password).isEmpty()
    }

    @Test
    void portShouldDefaultToEmpty() {
        assertThat(extension.port).isEqualTo(DEFAULT_PORT)
    }

    @Test
    void databaseNameShouldDefaultToEmpty() {
        assertThat(extension.databaseName).isEmpty()
    }

    @Test
    void versionShouldDefaultToDefaultVersion() {
        assertThat(extension.version).isEqualTo(DEFAULT_VERSION)
    }

    @Test
    void timeoutShouldDefaultToDefaultTimeout() {
        assertThat(extension.timeoutSeconds).isEqualTo(DEFAULT_TIMEOUT_SECONDS)
    }

    @Test
    void tempDirShouldDefaultToTarget() {
        assertThat(extension.tempDir).isEqualTo(DEFAULT_TEMP_DIR)
    }

    @Test
    void shouldSetDatabaseNameFromUrl() {
        extension.url = URL
        assertThat(extension.databaseName).isEqualTo(DATABASE_NAME)
    }

    @Test
    void shouldHandleSettingNullUrl() {
        extension.url = null
        assertThat(extension.port).isEqualTo(DEFAULT_PORT)
        assertThat(extension.databaseName).isEmpty()
    }

    @Test
    void shouldSetPortFromUrl() {
        extension.url = URL
        assertThat(extension.port).isEqualTo(OVERRIDE_PORT)
    }

    @Test
    void shouldSetUsername() {
        extension.username = OVERRIDE_USERNAME
        assertThat(extension.username).isEqualTo(OVERRIDE_USERNAME)
    }

    @Test
    void shouldSetPassword() {
        extension.password = PASSWORD
        assertThat(extension.password).isEqualTo(PASSWORD)
    }

    @Test
    void shouldSetVersion() {
        extension.version = OVERRIDE_VERSION.name()
        assertThat(extension.version).isEqualTo(OVERRIDE_VERSION)
    }

    @Test
    void shouldSetTimeoutSeconds() {
        extension.timeoutSeconds = OVERRIDE_TIMEOUT_SECONDS
        assertThat(extension.timeoutSeconds).isEqualTo(OVERRIDE_TIMEOUT_SECONDS)
    }

    @Test
    void shouldSetTempDir() {
        extension.tempDir = OVERRIDE_TEMP_DIR
        assertThat(extension.tempDir).isEqualTo(OVERRIDE_TEMP_DIR)
    }

    @Test
    void shouldThrowExceptionIfInvalidVersionSpecified() {
        def invalidVersion = "invalid version"

        Throwable thrown = catchThrowable({ extension.setVersion(invalidVersion) })

        assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
    }

    @Test
    void shouldSetCharset() {
        def charset = "cp866"
        extension.serverCharset = charset
        assertThat(extension.getServerCharset()).isEqualTo(charset)
    }

    @Test
    void shouldThrowExceptionIfInvalidCharset() {
        def invalid = "invalid"

        Throwable thrown = catchThrowable({ extension.setServerCharset(invalid) })

        assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
    }

    @Test
    void shouldSetCollate() {
        def collate = "gbk_chinese_ci"
        extension.serverCollate = collate
        assertThat(extension.getServerCollate()).isEqualTo(collate)
    }

    @Test
    void shouldThrowExceptionIfInvalidCollate() {
        def invalid = "invalid"

        Throwable thrown = catchThrowable({ extension.setServerCollate(invalid) })

        assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
    }

    @Test
    void shouldSetNullServerVariables() {
        extension.serverVars = null
        assertThat(extension.serverVars).isEmpty()
    }

    @Test
    void shouldSetBooleanVariable() {
        extension.serverVars = ["bool" : true]
        assertThat(extension.serverVars).containsKey("bool")
    }

    @Test
    void shouldSetIntegerVariable() {
        extension.serverVars = ["int" : 0]
        assertThat(extension.serverVars).containsKey("int")
    }

    @Test
    void shouldSetStringVariable() {
        extension.serverVars = ["str" : "any"]
        assertThat(extension.serverVars).containsKey("str")
    }

    @Test
    void shouldThrowExceptionIfInvalidVariableType() {
        Throwable thrown = catchThrowable({ extension.setServerVars(["any_key" : ["embedded_map" : true]]) })

        assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
    }

    @Test
    void shouldThrowExceptionIfInvalidVariableKey() {
        Throwable thrown = catchThrowable({ extension.setServerVars(["" : "any"]) })

        assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
    }

    @Test
    void shouldThrowExceptionIfInvalidVariableValue() {
        Throwable thrown = catchThrowable({ extension.setServerVars(["any" : ""]) })

        assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
    }

    @Test
    void shouldSetSchema() {
        def schema = "test1,test2"
        extension.schema = schema
        assertThat(extension.getSchema()).is(schema)
    }

    @Test
    void shouldIgnoreSchema() {
        def schema = " ,  "
        extension.schema = schema
        assertThat(extension.getSchema()).isEmpty()
    }

}
