start cmd.exe @cmd /c "java -cp C:\Users\NomeUtente\Documents\Progetto-M19-out main.Manager.Manager 1030"

timeout 1

start cmd.exe @cmd /c "java -cp C:\Users\NomeUtente\Documents\Progetto-M19-out main.Peripherals.Columns.EntryColumn 127.0.0.1 1030"

timeout 1

start cmd.exe @cmd /c "java -cp C:\Users\NomeUtente\Documents\Progetto-M19-out main.Peripherals.Cash.Cash 127.0.0.1 1030"

timeout 1

start cmd.exe @cmd /c "java -cp C:\Users\NomeUtente\Documents\Progetto-M19-out main.Peripherals.Columns.ExitColumn 127.0.0.1 1030"