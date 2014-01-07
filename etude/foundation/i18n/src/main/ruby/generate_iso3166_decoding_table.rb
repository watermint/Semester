require 'hpricot'
require 'open-uri'

doc = Hpricot(open('http://www.iso.org/iso/home/standards/country_codes/iso-3166-1_decoding_table.htm'))
enum_names = {
  'user-assigned' => 'UserAssigned',
  'exceptionally reserved' => 'ExceptionallyReserved',
  'officially assigned' => 'OfficiallyAssigned',
  'transitionally reserved' => 'TransitionallyReserved',
  'indeterminately reserved' => 'IntermediatelyReserved'
}
list = []

(doc / '//*[@id="country_codes"]/tbody/tr').each do |tr|
  code  = (tr / '//*[@data-label="Code"]').text
  state = (tr / '//*[@data-label="Status"]').text

  list << "\"#{code}\" -> CodeState.#{enum_names[state]}"
end

puts list.join(",\n")
