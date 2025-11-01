# Шаблон для Java-проектов 1 семестра Т-Академии

Описание задачи: [PROBLEM.md](./PROBLEM.md)

Для дополнительной справки: [HELP.md](./HELP.md)

## Запуск приложения (скрипты)

Для удобного запуска добавлены скрипты в корне проекта. Любые аргументы после имени скрипта передаются приложению `maze-app`.

- Требования для локального запуска: JDK (используется Maven Wrapper).
- Требования для Docker-запуска: установлен Docker.

### Linux / macOS

- Локально (Java):

```bash
./run-local.sh --help
./run-local.sh --version
./run-local.sh generate --help
./run-local.sh solve --help
./run-local.sh generate -a dfs -w 10 -h 10 -o maze.txt
./run-local.sh solve -a astar -f tests/cases/4_check_maze_solving/maze.txt -s 1,1 -e 9,9 -o out.txt
```

- В Docker:

```bash
./run-docker.sh --help
./run-docker.sh --version
./run-docker.sh generate --help
./run-docker.sh solve --help
./run-docker.sh generate -a dfs -w 10 -h 10 -o /tests/tmp/maze.txt
./run-docker.sh solve -a astar -f /tests/cases/4_check_maze_solving/maze.txt -s 1,1 -e 9,9 -o /tests/tmp/out.txt
```

### Windows

- PowerShell (используйте `.\` префикс — текущая папка не в PATH):

```cmd
.\run-local.cmd --help
.\run-local.cmd --version
.\run-local.cmd generate --help
.\run-local.cmd solve --help
.\run-local.cmd generate -a dfs -w 10 -h 10 -o maze.txt
.\run-local.cmd solve -a astar -f tests\cases\4_check_maze_solving\maze.txt -s 1,1 -e 9,9 -o out.txt
```

- CMD (префикс не требуется):

```cmd
run-local.cmd --help
run-local.cmd --version
run-local.cmd generate --help
run-local.cmd solve --help
run-local.cmd generate -a dfs -w 10 -h 10 -o maze.txt
run-local.cmd solve -a astar -f tests\cases\4_check_maze_solving\maze.txt -s 1,1 -e 9,9 -o out.txt
```

- В Docker:

  - PowerShell:

  ```cmd
  .\run-docker.cmd --help
  .\run-docker.cmd --version
  .\run-docker.cmd generate --help
  .\run-docker.cmd solve --help
  .\run-docker.cmd generate -a dfs -w 10 -h 10 -o /tests/tmp/maze.txt
  .\run-docker.cmd solve -a astar -f /tests/cases/4_check_maze_solving/maze.txt -s 1,1 -e 9,9 -o /tests/tmp/out.txt
  ```

  - CMD:

  ```cmd
  run-docker.cmd --help
  run-docker.cmd --version
  run-docker.cmd generate --help
  run-docker.cmd solve --help
  run-docker.cmd generate -a dfs -w 10 -h 10 -o /tests/tmp/maze.txt
  run-docker.cmd solve -a astar -f /tests/cases/4_check_maze_solving/maze.txt -s 1,1 -e 9,9 -o /tests/tmp/out.txt
  ```

## Эквиваленты без скриптов (локально)

Ниже команды для запуска напрямую через Maven Wrapper и `java` без скриптов. После генерации/решения показана команда для просмотра результата.

### Windows PowerShell

```cmd
# Сборка (без тестов)
.\mvnw.cmd -q -DskipTests package

# Генерация: DFS
java -cp .\target\project-1.0.jar academy.Application generate -a dfs -w 21 -h 11 -o .\maze_dfs.txt
type .\maze_dfs.txt

# Генерация: Prim
java -cp .\target\project-1.0.jar academy.Application generate -a prim -w 21 -h 11 -o .\maze_prim.txt
type .\maze_prim.txt

# Решение: A*
java -cp .\target\project-1.0.jar academy.Application solve -a astar -f .\maze_dfs.txt -s 2,2 -e 19,10 -o .\out_astar.txt
type .\out_astar.txt

# Решение: Dijkstra
java -cp .\target\project-1.0.jar academy.Application solve -a dijkstra -f .\maze_dfs.txt -s 2,2 -e 19,10 -o .\out_dijkstra.txt
type .\out_dijkstra.txt
```

### Linux / macOS

```bash
# Сборка (без тестов)
./mvnw -q -DskipTests package

# Генерация: DFS
java -cp ./target/project-1.0.jar academy.Application generate -a dfs -w 21 -h 11 -o maze_dfs.txt
cat maze_dfs.txt

# Генерация: Prim
java -cp ./target/project-1.0.jar academy.Application generate -a prim -w 21 -h 11 -o maze_prim.txt
cat maze_prim.txt

# Решение: A*
java -cp ./target/project-1.0.jar academy.Application solve -a astar -f ./maze_dfs.txt -s 2,2 -e 19,10 -o ./out_astar.txt
cat out_astar.txt

# Решение: Dijkstra
java -cp ./target/project-1.0.jar academy.Application solve -a dijkstra -f ./maze_dfs.txt -s 2,2 -e 19,10 -o ./out_dijkstra.txt
cat out_dijkstra.txt
```

Примечания:
- Координаты `-s` и `-e` должны приходиться на проходы (пробелы), а не на `#`.
- Для эстетичного узора стен используйте нечётные размеры (например, `21x11`).

## Запуск Black Box тестов

Тесты находятся в каталоге `tests/cases` и запускаются скриптом-раннером.

Предусловие: соберите Docker-образ `app` (единожды)

```bash
# Linux / macOS / WSL
./mvnw -q -DskipTests package
docker build -t app .
```

Запуск всех сценариев

- Linux / macOS:

```bash
tests/run.sh tests/cases
```

- Windows (WSL рекомендуется):

```bash
# из PowerShell (без входа в WSL)
wsl bash -lc "cd /mnt/c/workspace/t-academy/hw2-labyrinths && tests/run.sh tests/cases"

# или внутри терминала WSL
cd /mnt/c/workspace/t-academy/hw2-labyrinths
bash tests/run.sh tests/cases
```

Примечание (Windows): если видите сообщение `set: Illegal option -o pipefail`, запускайте раннер именно под bash (а не sh):

```bash
wsl bash -lc "cd /mnt/c/workspace/t-academy/hw2-labyrinths && tests/run.sh tests/cases"
# или
bash tests/run.sh tests/cases
```

Замечание для Windows (CRLF → LF)

Если видите ошибки вида `: not found` или странные строки вроде `' Unknown option: '--help`, это, как правило, из‑за переводов строк (CRLF). Исправление:

```bash
# в корне проекта (в WSL)
git config core.autocrlf false
git checkout -- tests tests/run.sh
# затем повторно
bash tests/run.sh tests/cases
```

Альтернатива:

```bash
sudo apt-get update && sudo apt-get install -y dos2unix
dos2unix tests/run.sh tests/cases/*/*.txt
bash tests/run.sh tests/cases
```
