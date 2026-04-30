# Subir CSV a la API
$csvPath = "c:\Users\osama\Desktop\ActivitatRa3Part1\productos.csv"
$uri = "http://localhost:8080/api/products/upload"

# Crear multipart form
$fileStream = [System.IO.File]::OpenRead($csvPath)
$boundary = [guid]::NewGuid().ToString()
$crlf = "`r`n"

# Headers
$headers = @{
    "Content-Type" = "multipart/form-data; boundary=$boundary"
}

# Build body
$body = New-Object System.IO.MemoryStream

# Add file part
$part = "--$boundary$crlf"
$part += "Content-Disposition: form-data; name=`"file`"; filename=`"$(Split-Path $csvPath -Leaf)`"$crlf"
$part += "Content-Type: text/csv$crlf$crlf"

$bodyBytes = [System.Text.Encoding]::UTF8.GetBytes($part)
$body.Write($bodyBytes, 0, $bodyBytes.Length)

# Add file content
$fileBytes = [System.IO.File]::ReadAllBytes($csvPath)
$body.Write($fileBytes, 0, $fileBytes.Length)

# Add closing boundary
$footer = "$crlf--$boundary--$crlf"
$footerBytes = [System.Text.Encoding]::UTF8.GetBytes($footer)
$body.Write($footerBytes, 0, $footerBytes.Length)

# Send request
$body.Seek(0, [System.IO.SeekOrigin]::Begin) | Out-Null
$response = Invoke-WebRequest -Uri $uri -Method Post -Headers $headers -Body $body.ToArray() -UseBasicParsing

Write-Host "Status Code: $($response.StatusCode)"
Write-Host "Response:"
$response.Content | ConvertFrom-Json | Format-Table -AutoSize
