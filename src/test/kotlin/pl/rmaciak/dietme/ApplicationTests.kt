package pl.rmaciak.dietme

import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.spring.SpringExtension
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import

@Import(TestcontainersConfiguration::class)
@SpringBootTest
class ApplicationTests :
	FunSpec({
		extension(SpringExtension)

		test("context loads") {
			// Spring context should load successfully
		}
	})
