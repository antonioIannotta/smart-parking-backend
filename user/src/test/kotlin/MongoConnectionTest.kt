import io.ktor.server.testing.*
import org.junit.jupiter.api.Test
import use_cases.getMongoClient
import use_cases.getUserCollection

class MongoConnectionTest {

    @Test
    fun `test connection to mongo atlas database and user collection existence`() = testApplication {

        val mongoClient = getMongoClient()
        getUserCollection(mongoClient)

    }

}