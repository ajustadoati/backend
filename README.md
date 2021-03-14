Pasos para arrancar backend
git clone https://github.com/ajustadoati/backend.git
cd backend
docker build backend-test .
//Se crea red
docker network create ajustadoati
//se arranca neo
docker run -d --name neo --network ajustadoati -p7474:7474 -p7687:7687 -e NEO4J_AUTH=neo4j/xxxx neo4j
//se arranca backend, se coloca la ruta donde fue descargado para el workdirectory
docker run -it --network ajustadoati --link neo  -v /Volumes/Data/backend:/app --name backend -p 9000:9000 backend-test
//Se ejecuta activador
//Aumentar a 0.13.6 project/build.properties
/usr/local/activator/activator stage
//si hay error arrancando borrar pid
rm -r /app/target/universal/stage/RUNNING_PID
//probar
http://localhost:9000/ajustadoati/categoria/
